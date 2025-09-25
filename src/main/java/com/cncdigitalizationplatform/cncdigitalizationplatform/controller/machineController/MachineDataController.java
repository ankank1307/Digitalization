package com.cncdigitalizationplatform.cncdigitalizationplatform.controller.machineController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.LogDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService.MachineDataParserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/machine-data")
public class MachineDataController {
    
    @Autowired
    private MachineDataParserService parserService;
    
    @PostMapping
    public ResponseEntity<String> parseMachineData(@RequestBody String jsonData) {
        try {
            List<LogDto> logDtos = parserService.parseData(jsonData);
            return ResponseEntity.ok("done");
            
        } catch (JsonProcessingException e) {
            System.err.println("JSON parsing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing machine data: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing data: " + e.getMessage());
        }
    }
}