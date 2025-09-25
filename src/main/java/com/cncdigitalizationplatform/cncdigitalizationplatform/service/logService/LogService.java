package com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.LogDto;

public interface LogService {
    LogDto addLog(LogDto logDto, String currentStatus);

    List<LogDto> getAllLogs();

    LogDto getLogById(String id);

    List<LogDto> getLogsByMachineId(String machineId);

    List<LogDto> getLogsByProcessId(String processId);

    List<LogDto> getLogsByAccountId(String accountId);
}
