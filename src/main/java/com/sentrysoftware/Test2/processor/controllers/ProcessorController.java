package com.sentrysoftware.Test2.processor.controllers;
import com.sentrysoftware.Test2.processor.models.ResultData;
import com.sentrysoftware.Test2.processor.services.ProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "processors")
public class ProcessorController {
    @Autowired
    private ProcessorService processorService;
    @GetMapping("/CPUprcrProcessorTimePercent/{operation}")
    public List<ResultData> getHistoricalDataSummary(@PathVariable String operation, @RequestParam int history) throws InterruptedException {
        if (operation.equals("max") || operation.equals("min") || operation.equals("avg")) {
            return processorService.getResults(history, operation);
        } else {
            throw new UnsupportedOperationException("Unsupported operation: " + operation);
        }
    }

}
