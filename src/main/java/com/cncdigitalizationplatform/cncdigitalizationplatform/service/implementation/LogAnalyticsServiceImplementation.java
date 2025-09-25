package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.TimeAnalysisDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Log;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService.LogAnalyticsService;

@Service
public class LogAnalyticsServiceImplementation implements LogAnalyticsService {
    
    @Autowired
    private LogRepository logRepository;
    
    @Override
    public TimeAnalysisDto getTimeAnalysis(String machineId, String accountId, 
                                            String processId, Long startTime, Long endTime) {
        
        final Long finalStartTime = startTime != null ? startTime : System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        final Long finalEndTime = endTime != null ? endTime : System.currentTimeMillis();
        
        // Validate that only one parameter is provided
        int parameterCount = 0;
        if (machineId != null) parameterCount++;
        if (accountId != null) parameterCount++;
        if (processId != null) parameterCount++;
        
        if (parameterCount != 1) {
            throw new IllegalArgumentException("Exactly one parameter (machineId, accountId, or processId) must be provided");
        }
        
        // Get filtered logs
        List<Log> logs = logRepository.findLogsWithFilters(machineId, accountId, processId, finalStartTime, finalEndTime);
        
        // Return single aggregated entry
        TimeAnalysisDto aggregatedResult = calculateAggregatedTimeAnalysis(logs, finalStartTime, finalEndTime, machineId, accountId, processId);
        return aggregatedResult;
    }

    private TimeAnalysisDto calculateAggregatedTimeAnalysis(List<Log> logs, Long analysisStartTime, Long analysisEndTime, 
                                                        String filterMachineId, String filterAccountId, String filterProcessId) {
        if (logs.isEmpty()) {
            return null;
        }
        
        logs.sort((a, b) -> Long.compare(a.getTimeStamp(), b.getTimeStamp()));
        
        TimeAnalysisDto analysis = new TimeAnalysisDto();
        
        // Set information based on which filter was provided
        if (filterMachineId != null) {
            analysis.setMachineId(filterMachineId);
        }
        
        if (filterAccountId != null) {
            analysis.setAccountId(filterAccountId);
        }
        
        if (filterProcessId != null) {
            analysis.setProcessId(filterProcessId);
        }
        
        analysis.setAnalysisStartTime(analysisStartTime);
        analysis.setAnalysisEndTime(analysisEndTime);
        
        // Calculate aggregated time durations
        long runTime = 0;
        long idleTime = 0;
        long errorTime = 0;
        
        for (int i = 0; i < logs.size(); i++) {
            Log currentLog = logs.get(i);
            
            long duration;
            if (i < logs.size() - 1) {
                duration = logs.get(i + 1).getTimeStamp() - currentLog.getTimeStamp();
            } else {
                duration = analysisEndTime - currentLog.getTimeStamp();
            }
            
            String status = currentLog.getMachineStatus();
            if (status != null) {
                if (status.equals("running")) {
                    runTime += duration;
                } else if (status.equals("error") || status.equals("emergency") || status.equals("alarm")) {
                    errorTime += duration;
                } else {
                    idleTime += duration;
                }
            } else {
                idleTime += duration;
            }
        }
        
        long totalTime = runTime + idleTime + errorTime;
        
        analysis.setRunTime(runTime);
        analysis.setIdleTime(idleTime);
        analysis.setErrorTime(errorTime);
        analysis.setTotalTime(totalTime);
        
        if (totalTime > 0) {
            analysis.setRunTimePercentage(Math.round((runTime * 100.0 / totalTime) * 100.0) / 100.0);
            analysis.setIdleTimePercentage(Math.round((idleTime * 100.0 / totalTime) * 100.0) / 100.0);
            analysis.setErrorTimePercentage(Math.round((errorTime * 100.0 / totalTime) * 100.0) / 100.0);
        } else {
            analysis.setRunTimePercentage(0.0);
            analysis.setIdleTimePercentage(0.0);
            analysis.setErrorTimePercentage(0.0);
        }
        
        return analysis;
    }
}
