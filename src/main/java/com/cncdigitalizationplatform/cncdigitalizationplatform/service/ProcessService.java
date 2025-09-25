package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessDto;

public interface ProcessService {
    ProcessDto addProcess(ProcessDto processDto);

    ProcessDto updateProcess(String id, ProcessDto processDto, String workingOperator);

    void deleteProcess(String id);

    List<ProcessDto> getAllProcesses();

    ProcessDto getProcessById(String id);
}
