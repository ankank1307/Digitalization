package com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.TimeAnalysisDto;

public interface LogAnalyticsService {
    TimeAnalysisDto getTimeAnalysis(String machineId, String accountId, String processId, Long startTime, Long endTime);
}
