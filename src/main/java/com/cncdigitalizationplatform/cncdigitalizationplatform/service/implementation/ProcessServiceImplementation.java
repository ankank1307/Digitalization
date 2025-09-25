package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.ProcessMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.DrawingCodeRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.ProcessService;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.ProcessTimeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProcessServiceImplementation implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private DrawingCodeRepository drawingRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessTimeService processTimeService;

    @Override
    public ProcessDto addProcess(ProcessDto processDto) {
        // Fetch related entities
        DrawingCode drawing = null;
        if (processDto.getDrawingCodeId() != null) {
            drawing = drawingRepository.findById(processDto.getDrawingCodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("drawing"));
        }

        Machine machine = null;
        if (processDto.getMachineId() != null) {
            machine = machineRepository.findById(processDto.getMachineId())
                    .orElseThrow(() -> new ResourceNotFoundException("machine"));
        }

        Process process = processMapper.toEntity(processDto, drawing, machine);
        Process savedProcess = processRepository.save(process);
        return processMapper.toDto(savedProcess);
    }

    @Override
    public List<ProcessDto> getAllProcesses() {
        return processMapper.toDtoList(processRepository.findAll());
    }

    @Override
    public ProcessDto getProcessById(String id) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("process"));
        return processMapper.toDto(process);
    }

    @Override
    public ProcessDto updateProcess(String id, ProcessDto processDto, String workingOperatorId) {
        Process existingProcess = processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("process"));

        processMapper.updateProcessFromDto(processDto, existingProcess);

        // Update related entities
        if (processDto.getDrawingCodeId() != null) {
            DrawingCode drawing = drawingRepository.findById(processDto.getDrawingCodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("drawing"));
            existingProcess.setDrawingCode(drawing);
        }

        if (processDto.getMachineId() != null) {
            Machine machine = machineRepository.findById(processDto.getMachineId())
                    .orElseThrow(() -> new ResourceNotFoundException("machine"));
            existingProcess.setMachine(machine);
        }
        if (processDto.getTaskStatus() != null) {
            if (processDto.getTaskStatus().equals("checked")) {
                processTimeService.calculateProcessTime(existingProcess);
            }
        }

        existingProcess.setUpdatedTime(System.currentTimeMillis());

        Process updatedProcess = processRepository.save(existingProcess);

        return processMapper.toDto(updatedProcess);
    }

    @Override
    public void deleteProcess(String id) {
        Process process = processRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("process"));
        processRepository.delete(process);
    }
}
