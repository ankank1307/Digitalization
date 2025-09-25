package com.cncdigitalizationplatform.cncdigitalizationplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.LogDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.TimeAnalysisDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService.LogAnalyticsService;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService.LogService;

import lombok.AllArgsConstructor;

@RequestMapping("api/logs")
@AllArgsConstructor
@RestController
public class LogController {
    private final LogService logService;
    private final LogAnalyticsService logAnalyticsService;

    @GetMapping("/time-analysis")
    public ResponseEntity<TimeAnalysisDto> getTimeAnalysis(
            @RequestParam(required = false) String machineId,
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String processId,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        // Analytics operations
        TimeAnalysisDto analysis = logAnalyticsService.getTimeAnalysis(machineId, accountId, processId, startTime,
                endTime);

        return ResponseEntity.ok(analysis);
    }

    @GetMapping
    public ResponseEntity<List<LogDto>> getAllLogs() {
        List<LogDto> machineLogs = logService.getAllLogs();
        return ResponseEntity.status(HttpStatus.OK).body(machineLogs);
    }

    @GetMapping("/{machineLogId}")
    public ResponseEntity<LogDto> getLog(@PathVariable("machineLogId") String machineLogId) {
        LogDto machineLog = logService.getLogById(machineLogId);
        return ResponseEntity.status(HttpStatus.OK).body(machineLog);
    }

    @GetMapping("/machine/{machineId}")
    public ResponseEntity<List<LogDto>> getLogsByMachine(@PathVariable("machineId") String machineId) {
        List<LogDto> logs = logService.getLogsByMachineId(machineId);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<LogDto>> getLogsByAccount(@PathVariable("accountId") String accountId) {
        List<LogDto> logs = logService.getLogsByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }

    @GetMapping("/process/{processId}")
    public ResponseEntity<List<LogDto>> getLogsByProcess(@PathVariable("processId") String processId) {
        List<LogDto> logs = logService.getLogsByProcessId(processId);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }

    // @PostMapping
    // public ResponseEntity<LogDto> addLog(@RequestBody LogDto logDto) {
    // LogDto savedLog = logService.addLog(logDto);
    // return ResponseEntity.status(HttpStatus.CREATED).body(savedLog);
    // }
}
