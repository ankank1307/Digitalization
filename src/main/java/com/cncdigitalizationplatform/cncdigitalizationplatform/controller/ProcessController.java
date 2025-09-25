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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.ProcessService;

import lombok.AllArgsConstructor;

@RequestMapping("api/processes")
@AllArgsConstructor
@RestController
public class ProcessController {
    private final ProcessService processService;

    @GetMapping
    public ResponseEntity<List<ProcessDto>> getAllProcesss() {
        List<ProcessDto> processs = processService.getAllProcesses();
        return ResponseEntity.status(HttpStatus.OK).body(processs);
    }

    @PostMapping
    public ResponseEntity<ProcessDto> addProcess(@RequestBody ProcessDto processDto) {
        ProcessDto process = processService.addProcess(processDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(process);
    }

    @PutMapping("/{processId}")
    public ResponseEntity<ProcessDto> updateProcess(@PathVariable("processId") String processId, @RequestBody ProcessDto processDto, @RequestParam(value = "workingOperatorId", required = false) String workingOperatorId) {
        ProcessDto updatedProcess = processService.updateProcess(processId, processDto, workingOperatorId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProcess);
    }

    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> deleteProcess(@PathVariable("processId") String processId) {
        processService.deleteProcess(processId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{processId}")
    public ResponseEntity<ProcessDto> getProcess(@PathVariable("processId") String processId) {
        ProcessDto process = processService.getProcessById(processId);
        return ResponseEntity.status(HttpStatus.OK).body(process);
    }
}