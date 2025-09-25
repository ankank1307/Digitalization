package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {
    private String id;
    private String accountId;
    private String machineId;
    private String machineStatus;
    private String processId;
    private Long timeStamp;
}
