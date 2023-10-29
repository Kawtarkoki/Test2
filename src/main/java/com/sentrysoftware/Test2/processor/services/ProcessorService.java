package com.sentrysoftware.Test2.processor.services;
import com.sentrysoftware.Test2.processor.models.ResultData;
import com.sentrysoftware.Test2.processor.utils.HTTPClient;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ProcessorService {
    public List<String> getAllProcessorIds() {
        final String DATA_URL = "https://xdemo.sentrysoftware.com/rest/namespace/NT_CPU";
        try {
            JsonNode rootNode = HTTPClient.get(DATA_URL);
            JsonNode subnodes = rootNode.get("subnodes");

            List<String> processorIds = new ArrayList<>();
            for (JsonNode node : subnodes) {
                String processorId = node.asText();
                if (!processorId.equals("CPU__Total")) {
                    processorIds.add(processorId);
                }
            }
            return processorIds;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve processor data", e);
        }
    }

    private double getCalculationResultForProcessor(String processorId, int history, String calculation) {

        String DATA_URL = "https://xdemo.sentrysoftware.com/rest/console/NT_CPU/" + processorId + "/CPUprcrProcessorTimePercent?max=" + history;

        try {
            JsonNode rootNode = HTTPClient.get(DATA_URL);
            JsonNode historyNode = rootNode.get("history");

            List<Double> historicalData = new ArrayList<>();
            for (JsonNode node : historyNode) {
                String value = node.get("value").asText();
                historicalData.add(Double.parseDouble(value));
            }

            switch (calculation) {
                case "min":
                    double minValue = historicalData.stream()
                            .mapToDouble(Double::doubleValue)
                            .min()
                            .orElse(0.0);
                    return minValue;
                case "max":
                    double maxValue = historicalData.stream()
                            .mapToDouble(Double::doubleValue)
                            .max()
                            .orElse(0.0);
                    return maxValue;
                case "avg":
                    double avgValue = historicalData.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);
                    return avgValue;
                default:
                    return 0.0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve historical data for processor " + processorId, e);
        }

    }

    public List<ResultData> getResults(int history, String calculation) throws InterruptedException {
        List<String> processors = this.getAllProcessorIds();
        List<ResultData> results = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(processors.size());

        Thread[] threads = new Thread[processors.size()];
        for (int i = 0; i < processors.size(); i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                ResultData rd = new ResultData(processors.get(index), this.getCalculationResultForProcessor(processors.get(index), history, calculation));
                results.add(rd);
                latch.countDown();
            });
            threads[i].start();
        }

        //Wait for all the threads to finish
        latch.await();

        //Sort the list in descending order by value
        results.sort(Comparator.comparingDouble(ResultData::value).reversed());
        return results;
    }
}
