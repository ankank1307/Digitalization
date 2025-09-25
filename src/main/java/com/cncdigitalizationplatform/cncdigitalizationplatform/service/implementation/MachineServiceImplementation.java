package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.MachineDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.MachineMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService.MachineService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MachineServiceImplementation implements MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachineMapper machineMapper;

    @Override
    public MachineDto addMachine(MachineDto machineDto) {
        machineDto.setStatus("idle");
        int isActive = 1;
        Machine machine = machineMapper.toEntity(machineDto);
        machine.setIsActive(isActive);
        Machine savedMachine = machineRepository.save(machine);

        return machineMapper.toDto(savedMachine);
    }

    @Override
    public List<MachineDto> getAllMachines() {
        return machineMapper.toDtoList(machineRepository.findAll());
    }

    @Override
    public MachineDto getMachineById(String id) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("machine"));

        return machineMapper.toDto(machine);
    }

    @Override
    public MachineDto updateMachine(String id, MachineDto machineDto) {
        Machine existingMachine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("machine"));

        machineMapper.updateMachineFromDto(machineDto, existingMachine);
        existingMachine.setUpdatedTime(System.currentTimeMillis());

        Machine updatedMachine = machineRepository.save(existingMachine);
        return machineMapper.toDto(updatedMachine);
    }

    @Override
    public void deleteMachine(String id) {
        Machine machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("machine"));
        machineRepository.delete(machine);

    }

    @Override
    public List<MachineDto> getIdleMachine() {
        return machineMapper.toDtoList(machineRepository.findMachineByStatus("idle"));
    }

    @Override
    public void changeMachineStatus() {

    }
}