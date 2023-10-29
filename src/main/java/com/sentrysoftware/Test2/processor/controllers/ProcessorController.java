package com.sentrysoftware.Test2.processor.controllers;

import com.sentrysoftware.Test2.processor.models.HistoricalDataDTO;
import com.sentrysoftware.Test2.processor.models.ProcessorDataSummary;
import com.sentrysoftware.Test2.processor.services.ProcessorHistoryService;
import com.sentrysoftware.Test2.processor.services.ProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "processors")
public class ProcessorController {
    @Autowired
    private ProcessorService processorService;

    @Autowired
    private ProcessorHistoryService processorHistoryService;

    @GetMapping
    public List<String> getProcessorData() {
        List<String> processorIds = processorService.getAllProcessorIds();
        return processorIds;
    }

    @GetMapping("/CPUprcrProcessorTimePercent")
    public CompletableFuture<ProcessorDataSummary> getHistoricalDataSummary(@RequestParam String processorId, @RequestParam int max, @RequestParam String calculation) {
        CompletableFuture<List<HistoricalDataDTO>> historicalData = processorHistoryService.getHistoricalDataForProcessor(processorId, max);

        return historicalData.thenApply(data -> {
            double result = 0.0;
            if ("min".equals(calculation)) {
                result = processorHistoryService.calculateMinValue(data);
            } else if ("max".equals(calculation)) {
                result = processorHistoryService.calculateMaxValue(data);
            } else if ("avg".equals(calculation)) {
                result = processorHistoryService.calculateAvgValue(data);
            }

            return new ProcessorDataSummary(processorId, calculation, result);
        });
    }

}
