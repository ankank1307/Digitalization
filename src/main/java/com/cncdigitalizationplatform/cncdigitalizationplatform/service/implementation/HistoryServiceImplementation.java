package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.HistoryDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.History;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.HistoryMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.HistoryRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.HistoryService;

@Service
public class HistoryServiceImplementation implements HistoryService {
    
    @Autowired
    private HistoryRepository historyRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private HistoryMapper historyMapper;
    
    @Override
    public HistoryDto addHistory(HistoryDto historyDto) {        
        Account account = accountRepository.findById(historyDto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("account"));
        
        History history = historyMapper.toEntity(historyDto, account);
        History savedHistory = historyRepository.save(history);
        
        return historyMapper.toDto(savedHistory);
    }

    @Override
    public List<HistoryDto> getAllHistorys() {
        return historyMapper.toDtoList(historyRepository.findAll());
    }

    @Override
    public HistoryDto getHistoryById(String historyId) {
        History history = historyRepository.findById(historyId)
            .orElseThrow(() -> new ResourceNotFoundException("history"));        
        return historyMapper.toDto(history);
    }

    @Override
    public void deleteHistory(String historyId) {
        History history = historyRepository.findById(historyId)
            .orElseThrow(() -> new ResourceNotFoundException("history"));
        historyRepository.delete(history);
    }

    @Override
    public List<HistoryDto> getHistoryByFilters(String accountId, String operationType, String dataType) {
        List<History> histories;
        
        // All three filters
        if (accountId != null && operationType != null && dataType != null) {
            histories = historyRepository.findByAccount_IdAndOperationTypeAndDataTypeOrderByTimeDesc(
                accountId, operationType, dataType);
        }
        // Two filters: accountId + operationType
        else if (accountId != null && operationType != null) {
            histories = historyRepository.findByAccount_IdAndOperationTypeOrderByTimeDesc(accountId, operationType);
        }
        // Two filters: accountId + dataType
        else if (accountId != null && dataType != null) {
            histories = historyRepository.findByAccount_IdAndDataTypeOrderByTimeDesc(accountId, dataType);
        }
        // Two filters: operationType + dataType
        else if (operationType != null && dataType != null) {
            histories = historyRepository.findByOperationTypeAndDataTypeOrderByTimeDesc(operationType, dataType);
        }
        // Single filters
        else if (accountId != null) {
            histories = historyRepository.findByAccount_IdOrderByTimeDesc(accountId);
        }
        else if (operationType != null) {
            histories = historyRepository.findByOperationTypeOrderByTimeDesc(operationType);
        }
        else if (dataType != null) {
            histories = historyRepository.findByDataTypeOrderByTimeDesc(dataType);
        }
        // No filters - get all
        else {
            histories = historyRepository.findAllByOrderByTimeDesc();
        }
        
        return historyMapper.toDtoList(histories);
    }
}