package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.DrawingCodeDto;

public interface DrawingCodeService {
    DrawingCodeDto addDrawingCode(DrawingCodeDto drawingCodeDto);

    DrawingCodeDto updateDrawingCode(String id, DrawingCodeDto drawingCodeDto);

    void deleteDrawingCode(String id);

    List<DrawingCodeDto> getAllDrawingCodes();

    DrawingCodeDto getDrawingCodeById(String id);
}
