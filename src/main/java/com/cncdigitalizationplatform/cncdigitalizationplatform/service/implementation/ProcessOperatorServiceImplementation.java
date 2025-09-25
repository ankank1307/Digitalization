package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessOperatorDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessOperator;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.ProcessOperatorMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessOperatorRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.ProcessOperatorService;

@Service
public class ProcessOperatorServiceImplementation implements ProcessOperatorService {
    
    @Autowired
    ProcessOperatorRepository processOperatorRepository;
    
    @Autowired
    ProcessRepository processRepository;
    
    @Autowired
    AccountRepository accountRepository;

    @Override
    public ProcessOperatorDto addProcessOperator(ProcessOperatorDto processOperatorDto) {
        Process process = processRepository.findById(processOperatorDto.getProcessId())
                .orElseThrow(() -> new RuntimeException("process"));
                
        ProcessOperator processOperator = new ProcessOperator();
        
        if (processOperatorDto.getAccountId() != null) {
            Account account = accountRepository.findById(processOperatorDto.getAccountId())
                .orElseThrow(() -> new RuntimeException("account"));
            processOperator.setAccount(account);
            
            if (!account.getRole().getRoleShort().equals("op")) {
                throw new IllegalArgumentException("notOperator");
            }
        }

        processOperator.setProcess(process);
        
        ProcessOperator savedProcessOperator = processOperatorRepository.save(processOperator);
        return ProcessOperatorMapper.toDto(savedProcessOperator);
    }

    @Override
    public List<ProcessOperatorDto> getAllProcessOperators() {
        List<ProcessOperator> processOperators = processOperatorRepository.findAll();
        return processOperators.stream().map(processOperator -> {
            return ProcessOperatorMapper.toDto(processOperator);
        }).toList();
    }

    @Override
    public ProcessOperatorDto getProcessOperatorById(String processOperatorId) {
        ProcessOperator processOperator = processOperatorRepository.findById(processOperatorId)
                .orElseThrow(() -> new RuntimeException("processOperator"));
        return ProcessOperatorMapper.toDto(processOperator);
    }
}