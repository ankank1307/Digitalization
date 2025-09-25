package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;

public interface DrawingCodeRepository extends JpaRepository<DrawingCode, String>{
    Optional<DrawingCode> findByName(String name);
}
