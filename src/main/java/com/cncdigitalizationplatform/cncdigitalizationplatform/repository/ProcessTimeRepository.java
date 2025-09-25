package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessTime;

public interface ProcessTimeRepository extends JpaRepository<ProcessTime, String> {
    ProcessTime findByProcess_Id(String id);
}
