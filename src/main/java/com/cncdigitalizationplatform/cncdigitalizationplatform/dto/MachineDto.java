package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineDto {
    private String id;
    private String name;
    private String imageUrl;
    private String status;
    private int isActive;
    private Long createdTime;
    private Long updatedTime;
}
