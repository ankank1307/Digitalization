package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessTime;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessTimeRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;

@Service
@AllArgsConstructor
public class ProcessTimeService {
    ProcessTimeRepository processTimeRepository;
    LogRepository logRepository;

    public void calculateProcessTime(Process process) {
        Object[] row = (Object[]) logRepository.calculateRunningAndIdleTimeByProcess(process.getId());

        Double runningMinutes = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        Double idleMinutes = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;

        ProcessTime processTime = new ProcessTime();
        processTime.setProcess(process);
        processTime.setRunTime(runningMinutes);
        processTime.setStopTime(idleMinutes);

        processTimeRepository.save(processTime);
    }
}
