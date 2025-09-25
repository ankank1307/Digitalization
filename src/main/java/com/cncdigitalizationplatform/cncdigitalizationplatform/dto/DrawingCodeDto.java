package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingCodeDto {
    private String id;
    private String name;
    private String pdfUrl;
    private String description;
    private String status;
    private Long createdTime;
    private Long updatedTime;
}
