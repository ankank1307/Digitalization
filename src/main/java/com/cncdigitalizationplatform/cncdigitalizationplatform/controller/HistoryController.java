package com.cncdigitalizationplatform.cncdigitalizationplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.HistoryDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.HistoryService;

import lombok.AllArgsConstructor;

@RequestMapping("api/histories")
@AllArgsConstructor
@RestController
public class HistoryController {
    private final HistoryService historyService;
    
    @GetMapping
    public ResponseEntity<List<HistoryDto>> getHistoriesByFilters(
            @RequestParam(value = "accountId", required = false) String accountId,
            @RequestParam(value = "operationType", required = false) String operationType,
            @RequestParam(value = "dataType", required = false) String dataType) {
            
        if (accountId != null || operationType != null || dataType != null) {
            List<HistoryDto> histories = historyService.getHistoryByFilters(accountId, operationType, dataType);
            return ResponseEntity.status(HttpStatus.OK).body(histories);
        }
        List<HistoryDto> histories = historyService.getAllHistorys();
        return ResponseEntity.status(HttpStatus.OK).body(histories);        
    }

    @PostMapping
    public ResponseEntity<HistoryDto> addHistory(@RequestBody HistoryDto historyDto) {
        HistoryDto savedHistory = historyService.addHistory(historyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHistory);
    }

    @GetMapping("/{historyId}")
    public ResponseEntity<HistoryDto> getHistory(@PathVariable("historyId") String historyId) {
        HistoryDto history = historyService.getHistoryById(historyId);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    @DeleteMapping("/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable("historyId") String historyId) {
        historyService.deleteHistory(historyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}