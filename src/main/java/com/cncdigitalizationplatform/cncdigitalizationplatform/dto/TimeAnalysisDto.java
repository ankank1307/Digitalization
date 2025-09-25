package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeAnalysisDto {
    private String machineId;
    private String accountId;
    private String processId;
    
    private long runTime;
    private long idleTime;
    private long errorTime;
    private long totalTime; // this is runTime + idleTime + errorTime
    private double runTimePercentage;
    private double idleTimePercentage;
    private double errorTimePercentage;
    
    private Long analysisStartTime;
    private Long analysisEndTime;
}
