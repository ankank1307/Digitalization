package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {
    private String id;
    private String accountId;
    private String operationType;
    private String description;
    private Long time;
    private String dataType;
}
