package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessOperator;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;

public interface ProcessOperatorRepository extends JpaRepository<ProcessOperator, String> {
    List<ProcessOperator> findByAccountIsNotNull();
    boolean existsByProcessAndAccountIsNull(Process process);
    List<ProcessOperator> findByAccountIsNotNullAndCreatedTimeGreaterThan(Long timeStamp);
    ProcessOperator findTopByProcessOrderByCreatedTimeDesc(Process process);
}
