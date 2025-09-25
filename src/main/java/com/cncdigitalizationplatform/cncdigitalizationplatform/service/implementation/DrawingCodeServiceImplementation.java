package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.DrawingCodeDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.DrawingCodeMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.DrawingCodeRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.DrawingCodeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DrawingCodeServiceImplementation implements DrawingCodeService {
    
    @Autowired
    private DrawingCodeRepository drawingCodeRepository;
    
    @Autowired
    private DrawingCodeMapper drawingCodeMapper;
    
    @Override
    public DrawingCodeDto addDrawingCode(DrawingCodeDto drawingDto) {        
        DrawingCode drawing = drawingCodeMapper.toEntity(drawingDto);
        DrawingCode savedDrawing = drawingCodeRepository.save(drawing);
        return drawingCodeMapper.toDto(savedDrawing);
    }

    @Override
    public List<DrawingCodeDto> getAllDrawingCodes() {
        return drawingCodeMapper.toDtoList(drawingCodeRepository.findAll());
    }

    @Override
    public DrawingCodeDto getDrawingCodeById(String id) {
        DrawingCode drawing = drawingCodeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("drawing"));
        
        return drawingCodeMapper.toDto(drawing);
    }

    @Override
    public DrawingCodeDto updateDrawingCode(String id, DrawingCodeDto drawingDto) {
        DrawingCode existingDrawing = drawingCodeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("drawing"));
         
        drawingCodeMapper.updateDrawingCodeFromDto(drawingDto, existingDrawing);
        existingDrawing.setUpdatedTime(System.currentTimeMillis());
        
        DrawingCode updatedDrawing = drawingCodeRepository.save(existingDrawing);
        return drawingCodeMapper.toDto(updatedDrawing);
    }

    @Override
    public void deleteDrawingCode(String id) {
        DrawingCode drawingCode = drawingCodeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("drawing"));
        drawingCodeRepository.delete(drawingCode);
    }
}