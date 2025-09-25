package com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.MachineDto;

public interface MachineService {
    MachineDto addMachine(MachineDto machineDto);

    MachineDto updateMachine(String id, MachineDto machineDto);

    void deleteMachine(String id);

    List<MachineDto> getAllMachines();

    MachineDto getMachineById(String id);

    List<MachineDto> getIdleMachine();

    void changeMachineStatus();
}
