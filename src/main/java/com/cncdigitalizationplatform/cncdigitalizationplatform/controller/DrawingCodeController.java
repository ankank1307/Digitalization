package com.cncdigitalizationplatform.cncdigitalizationplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.DrawingCodeDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.DrawingCodeService;

import lombok.AllArgsConstructor;

@RequestMapping("api/drawing-codes")
@AllArgsConstructor
@RestController
public class DrawingCodeController {
    private final DrawingCodeService drawingCodeService;

    @GetMapping
    public ResponseEntity<List<DrawingCodeDto>> getAllDrawings() {
        List<DrawingCodeDto> drawingCodes = drawingCodeService.getAllDrawingCodes();
        return ResponseEntity.status(HttpStatus.OK).body(drawingCodes);
    }

    @PostMapping
    public ResponseEntity<DrawingCodeDto> addDrawing(@RequestBody DrawingCodeDto drawingCodeDto) {
        DrawingCodeDto drawingCode = drawingCodeService.addDrawingCode(drawingCodeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(drawingCode);
    }

    // chinh sua drawing code
    @PutMapping("/{drawingCodeId}")
    public ResponseEntity<DrawingCodeDto> updateDrawing(@PathVariable("drawingCodeId") String drawingCodeId,
            @RequestBody DrawingCodeDto drawingCodeDto) {
        DrawingCodeDto updatedDrawing = drawingCodeService.updateDrawingCode(drawingCodeId, drawingCodeDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDrawing);
    }

    @DeleteMapping("/{drawingCodeId}")
    public ResponseEntity<Void> deleteDrawing(@PathVariable("drawingCodeId") String drawingCodeId) {
        drawingCodeService.deleteDrawingCode(drawingCodeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{drawingCodeId}")
    public ResponseEntity<DrawingCodeDto> getDrawing(@PathVariable("drawingCodeId") String drawingCodeId) {
        DrawingCodeDto drawingCode = drawingCodeService.getDrawingCodeById(drawingCodeId);
        return ResponseEntity.status(HttpStatus.OK).body(drawingCode);
    }
}