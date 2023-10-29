package com.sentrysoftware.Test2.processor.services.impl;
import com.sentrysoftware.Test2.processor.models.HistoricalDataDTO;
import com.sentrysoftware.Test2.processor.models.HistoricalDataResponse;
import com.sentrysoftware.Test2.processor.services.ProcessorHistoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
@Service
public class ProcessorHistoryServiceImpl implements ProcessorHistoryService {
    @Autowired
    private RestTemplate restTemplate;
    public CompletableFuture<List<HistoricalDataDTO>> getHistoricalDataForProcessor(String processorId, int max) {
        String url = "https://xdemo.sentrysoftware.com/rest/console/NT_CPU/" + processorId + "/CPUprcrProcessorTimePercent?max=" + max;

        ResponseEntity<HistoricalDataResponse> response = restTemplate.getForEntity(url, HistoricalDataResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            HistoricalDataResponse historicalDataResponse = response.getBody();
            var historicalData = historicalDataResponse.history();
            return CompletableFuture.completedFuture(historicalData);
        } else {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }
    @Override
    public double calculateMinValue(@NotNull List<HistoricalDataDTO> historicalData) {
        double minValue = historicalData.stream()
                .mapToDouble(HistoricalDataDTO::value)
                .min()
                .orElse(0.0);
        System.out.println("Min Value: " + minValue);
        return minValue;
    }
    public double calculateMaxValue(@NotNull List<HistoricalDataDTO> historicalData) {
        double maxValue = historicalData.stream()
                .mapToDouble(HistoricalDataDTO::value)
                .max()
                .orElse(0.0);
        System.out.println("Max Value: " + maxValue);
        return maxValue;
    }
    public double calculateAvgValue(@NotNull List<HistoricalDataDTO> historicalData) {
        double avgValue = historicalData.stream()
                .mapToDouble(HistoricalDataDTO::value)
                .average()
                .orElse(0.0);
        System.out.println("Average Value: " + avgValue);
        return avgValue;
    }

}
