package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;

public interface MachineRepository extends JpaRepository<Machine, String> {
    Machine findMachineByName(String name);

    List<Machine> findMachineByStatus(String status);
}
