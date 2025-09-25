package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessOperatorDto {
    private String id;
    private String processId;
    private String accountId;
    private Long createdTime;
}