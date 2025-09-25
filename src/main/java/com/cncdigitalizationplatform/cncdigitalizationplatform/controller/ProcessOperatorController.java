package com.cncdigitalizationplatform.cncdigitalizationplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessOperatorDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.ProcessOperatorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/process-operators")
public class ProcessOperatorController {
    private final ProcessOperatorService processOperatorService;

    @GetMapping
    public ResponseEntity<List<ProcessOperatorDto>> getAllProcessOperators() {
        List<ProcessOperatorDto> processOperators = processOperatorService.getAllProcessOperators();
        return ResponseEntity.status(HttpStatus.OK).body(processOperators);
    }

    @PostMapping
    public ResponseEntity<ProcessOperatorDto> addProcessOperator(@RequestBody ProcessOperatorDto processOperatorDto) {
        ProcessOperatorDto processOperator = processOperatorService.addProcessOperator(processOperatorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(processOperator);
    }

    @GetMapping("/{processOperatorId}")
    public ResponseEntity<ProcessOperatorDto> getProcessOperator(@PathVariable("processOperatorId") String processOperatorId) {
        ProcessOperatorDto processOperator = processOperatorService.getProcessOperatorById(processOperatorId);
        return ResponseEntity.status(HttpStatus.OK).body(processOperator);
    }
}