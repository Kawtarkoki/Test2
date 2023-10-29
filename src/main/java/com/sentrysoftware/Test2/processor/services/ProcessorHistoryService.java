package com.sentrysoftware.Test2.processor.services;
import com.sentrysoftware.Test2.processor.models.HistoricalDataDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProcessorHistoryService {
    CompletableFuture<List<HistoricalDataDTO>> getHistoricalDataForProcessor(String processorId, int max);
    double calculateMinValue(List<HistoricalDataDTO> historicalData);
    double calculateMaxValue(List<HistoricalDataDTO> historicalData);
    double calculateAvgValue(List<HistoricalDataDTO> historicalData);

}
