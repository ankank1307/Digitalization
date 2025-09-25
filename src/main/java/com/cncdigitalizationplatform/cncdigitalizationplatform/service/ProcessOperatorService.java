package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessOperatorDto;

public interface ProcessOperatorService {
    ProcessOperatorDto addProcessOperator(ProcessOperatorDto processOperatorDto);
    
    List<ProcessOperatorDto> getAllProcessOperators();
    
    ProcessOperatorDto getProcessOperatorById(String processOperatorId);
}
