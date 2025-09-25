package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.LogDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Log;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessOperator;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.LogMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessOperatorRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService.LogService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LogServiceImplementation implements LogService {
    @Autowired
    private LogRepository logRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessOperatorRepository processOperatorRepository;

    @Autowired
    private LogMapper logMapper;

    @Override
    public LogDto addLog(LogDto logDto, String currentStatus) {
        Log log = new Log();

        Machine machine = machineRepository.findById(logDto.getMachineId())
                .orElseThrow(() -> new ResourceNotFoundException("machine"));
        Account account = null;
        Process process = processRepository.findById(logDto.getProcessId()).orElse(null);
        Log savedLog = null;
        if (logDto.getAccountId() != null) {
            account = accountRepository.findById(logDto.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("account"));
            log.setAccount(account);
        } else {
            account = processOperatorRepository.findTopByProcessOrderByCreatedTimeDesc(process).getAccount();
            log.setAccount(account);
        }

        if (logDto.getMachineStatus().equals(currentStatus)) {
            System.out.println("There is nothing change");
        } else {
            log.setMachineStatus(logDto.getMachineStatus());
            log.setTimeStamp(logDto.getTimeStamp());
            log.setMachine(machine);
            log.setAccount(account);
            log.setProcess(process);
            savedLog = logRepository.save(log);
        }

        logMapper.toEntity(logDto, machine, account, process);

        return logMapper.toDto(savedLog);
    }

    @Override
    public List<LogDto> getAllLogs() {
        return logMapper.toDtoList(logRepository.findAll());
    }

    @Override
    public LogDto getLogById(String id) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("log"));
        return logMapper.toDto(log);
    }

    @Override
    public List<LogDto> getLogsByMachineId(String machineId) {
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new ResourceNotFoundException("machine"));
        List<Log> logs = logRepository.findByMachine_Id(machineId);
        return logMapper.toDtoList(logs);
    }

    @Override
    public List<LogDto> getLogsByProcessId(String processId) {
        Process process = processRepository.findById(processId)
                .orElseThrow(() -> new ResourceNotFoundException("process"));
        List<Log> logs = logRepository.findByProcess_Id(processId);
        return logMapper.toDtoList(logs);
    }

    @Override
    public List<LogDto> getLogsByAccountId(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("account"));
        List<Log> logs = logRepository.findByAccount_Id(accountId);
        return logMapper.toDtoList(logs);
    }
}
