package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.History;

public interface HistoryRepository extends JpaRepository<History, String> {
    List<History> findByAccount_IdOrderByTimeDesc(String accountId);
    List<History> findByOperationTypeOrderByTimeDesc(String operationType);
    List<History> findByDataTypeOrderByTimeDesc(String dataType);
    List<History> findByAccount_IdAndOperationTypeOrderByTimeDesc(String accountId, String operationType);
    List<History> findByAccount_IdAndDataTypeOrderByTimeDesc(String accountId, String dataType);
    List<History> findByOperationTypeAndDataTypeOrderByTimeDesc(String operationType, String dataType);
    List<History> findByAccount_IdAndOperationTypeAndDataTypeOrderByTimeDesc(String accountId, String operationType, String dataType);
    List<History> findAllByOrderByTimeDesc();
}
