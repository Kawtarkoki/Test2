package com.sentrysoftware.Test2.processor.services;
import com.sentrysoftware.Test2.processor.utils.HTTPClient;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

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
}
