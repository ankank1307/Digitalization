package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.HistoryDto;

public interface HistoryService {
    HistoryDto addHistory(HistoryDto historyDto);

    void deleteHistory(String id);

    List<HistoryDto> getAllHistorys();

    HistoryDto getHistoryById(String id);
    
    List<HistoryDto> getHistoryByFilters(String accountId, String operationType, String dataType);
}
