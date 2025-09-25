package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDto {
    private String id;
    private String drawingCodeId;
    private String machineId;
    private String name;
    private Long predictedDuration;
    private Long startTime;
    private Long endTime;
    private String taskStatus;
    private String approved;
    private String qcRemarks;
    private String status;
    private Long createdTime;
    private Long updatedTime;
}
