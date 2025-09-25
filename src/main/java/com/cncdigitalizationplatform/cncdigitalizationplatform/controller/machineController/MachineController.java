package com.cncdigitalizationplatform.cncdigitalizationplatform.controller.machineController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.MachineDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService.MachineService;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService.OeeExcelService;

import lombok.AllArgsConstructor;

@RequestMapping("api/machines")
@AllArgsConstructor
@RestController
public class MachineController {
    private final MachineService machineService;
    private final OeeExcelService oeeExcelService;
    private final MachineRepository machineRepository;

    @GetMapping
    public ResponseEntity<List<MachineDto>> getAllMachines() {
        List<MachineDto> machines = machineService.getAllMachines();
        return ResponseEntity.status(HttpStatus.OK).body(machines);
    }

    @PostMapping
    public ResponseEntity<MachineDto> addMachine(@RequestBody MachineDto machineDto) {
        MachineDto machine = machineService.addMachine(machineDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(machine);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateMachines(@RequestBody List<MachineDto> machineDtos) {
        List<MachineDto> updatedMachines = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < machineDtos.size(); i++) {
            MachineDto machineDto = machineDtos.get(i);
            try {
                if (machineDto.getId() == null || machineDto.getId().trim().isEmpty()) {
                    errors.add("Index " + i + ": Machine ID cannot be null or empty");
                    continue;
                }
                MachineDto updatedMachine = machineService.updateMachine(machineDto.getId(), machineDto);
                updatedMachines.add(updatedMachine);
            } catch (Exception e) {
                errors.add("Index " + i + " (ID: " + machineDto.getId() + "): " + e.getMessage());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("updatedMachines", updatedMachines);
        response.put("errors", errors);
        response.put("totalRequested", machineDtos.size());
        response.put("successfulUpdates", updatedMachines.size());
        response.put("failedUpdates", errors.size());

        HttpStatus status = errors.isEmpty() ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/{machineId}")
    public ResponseEntity<MachineDto> updateMachine(@PathVariable("machineId") String machineId,
            @RequestBody MachineDto machineDto) {
        MachineDto updatedMachine = machineService.updateMachine(machineId, machineDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMachine);
    }

    @DeleteMapping("/{machineId}")
    public ResponseEntity<Void> deleteMachine(@PathVariable("machineId") String machineId) {
        machineService.deleteMachine(machineId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oee")
    public ResponseEntity<List<OeeExcelService.MachineOEEData>> getAllMachinesOEE(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        try {
            // Default to last 24 hours if not provided
            Long finalStartDate = startDate != null ? startDate : System.currentTimeMillis() - (24 * 60 * 60 * 1000);
            Long finalEndDate = endDate != null ? endDate : System.currentTimeMillis();

            // Log start of data retrieval
            int totalMachines = (int) machineRepository.count();
            System.out.println("Starting OEE data retrieval for " + totalMachines + " machines");

            List<OeeExcelService.MachineOEEData> machineData = oeeExcelService.getAllMachinesOEEData(finalStartDate,
                    finalEndDate);

            System.out.println("Completed OEE data retrieval for all machines");

            return ResponseEntity.ok(machineData);

        } catch (Exception e) {
            System.err.println("Error during OEE data retrieval: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{machineId}")
    public ResponseEntity<MachineDto> getMachine(@PathVariable("machineId") String machineId) {
        MachineDto machine = machineService.getMachineById(machineId);
        return ResponseEntity.status(HttpStatus.OK).body(machine);
    }

    @GetMapping("/idle")
    public ResponseEntity<List<MachineDto>> getMachine() {
        List<MachineDto> machines = machineService.getIdleMachine();
        return ResponseEntity.status(HttpStatus.OK).body(machines);
    }
}