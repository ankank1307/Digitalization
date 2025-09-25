package com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.config.MachineConfig;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.LogDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Log;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MachineDataParserService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private LogRepository logRepository;
    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private LogService logService;

    public List<LogDto> parseData(String jsonString) throws JsonProcessingException {
        List<LogDto> logs = new ArrayList<>();

        JsonNode root = objectMapper.readTree(jsonString);

        // Parse timestamp
        // String rawTimestamp = root.get("timestamp").asText();
        // String cleanedTimestamp = rawTimestamp.length() > 23 ?
        // rawTimestamp.substring(0, 23) + "Z" : rawTimestamp + "Z";
        // Long timestamp = Instant.parse(cleanedTimestamp).toEpochMilli();

        String rawTimestamp = root.get("timestamp").asText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(rawTimestamp, formatter);

        long timestamp = localDateTime
                .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                .toInstant()
                .toEpochMilli();

        // Get the data node
        JsonNode dataNode = root.get("data");

        // Process all machine entries in the data object
        Iterator<String> fieldNames = dataNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();

            LogDto logDto = new LogDto();
            logDto.setTimeStamp(timestamp);
            // Extract machine name from field name (e.g., "fanuc_status_run2" -> "fanuc2")
            String machineName = extractMachineName(fieldName);

            Machine machine = machineRepository.findMachineByName(machineName);
            if (machine != null) {
                Process process = processRepository.findByMachine_IdAndTaskStatus(machine.getId(), "in_progress")
                        .orElse(null);
                if (process != null) {
                    logDto.setMachineId(machine.getId());
                    JsonNode machineNode = dataNode.get(fieldName);
                    int statusValue = machineNode.get("value").asInt();

                    logDto.setProcessId(process.getId());
                    String machineStatus = determineMachineStatus(statusValue);
                    logDto.setMachineStatus(machineStatus);
                    // Check if the most recent log entry has the same status
                    Optional<Log> mostRecentLog = logRepository
                            .findFirstByMachine_IdOrderByTimeStampDesc(machine.getId());

                    if (mostRecentLog.isPresent()) {
                        if (!machineStatus.equals(mostRecentLog.get().getMachineStatus())) {
                            logService.addLog(logDto, mostRecentLog.get().getMachineStatus());
                            logs.add(logDto); // Add to return list
                            System.out
                                    .println("Created log for machine: " + machineName + " with status: "
                                            + machineStatus);
                        } else {
                            System.out.println(
                                    "Skipped log for machine: " + machineName + " (same status: " + machineStatus
                                            + ")");
                        }
                    } else {
                        // No previous logs, create new one
                        logService.addLog(logDto, "");
                        process.setStartTime(timestamp);
                        logs.add(logDto); // Add to return list
                        System.out.println(
                                "Created first log for machine: " + machineName + " with status: " + machineStatus);
                    }
                } else {
                    System.err.println("Machine not found: " + machineName);
                }
            } else {
                System.out.println("Machine not found: " + machineName);
            }

        }
        return logs;
    }

    public List<MachineConfig> returnStatus(String jsonString) throws JsonProcessingException {
        List<MachineConfig> machineConfigs = new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonString);
        JsonNode dataNode = root.get("data");
        Iterator<String> fieldNames = dataNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            String machineName = extractMachineName(fieldName);
            JsonNode machineNode = dataNode.get(fieldName);
            int statusValue = machineNode.get("value").asInt();
            String machineStatus = determineMachineStatus(statusValue);
            Machine machine = machineRepository.findMachineByName(machineName);
            machine.setStatus(machineStatus);
            machineRepository.save(machine);
            MachineConfig machineConfig = new MachineConfig(machine.getId(), machineStatus);
            machineConfigs.add(machineConfig);
        }
        return machineConfigs;
    }

    /**
     * Extracts machine name from field name
     * Examples:
     * "fanuc_status_run" -> "fanuc"
     * "fanuc_status_run2" -> "fanuc2"
     */
    private String extractMachineName(String fieldName) {
        // // Remove "_status_run" suffix and extract machine name with number
        String machinePart = fieldName.replaceFirst("_status_run.*", "");

        // // Check if there's a number at the end of the remaining fieldName
        // if (fieldName.matches(".*_status_run\\d+$")) {
        // // Extract the number from the original fieldName
        // String numberPart = fieldName.replaceFirst(".*_status_run(\\d+)$", "$1");
        // return machinePart + numberPart;
        // }

        if (fieldName == "fanuc_status_run2") {
            machinePart = "Doosan 2";
        } else {
            machinePart = "Doosan 1";
        }
        return machinePart;
    }

    /**
     * Determines machine status based on integer value
     * Running group: 2, 4, 5, 6, 13
     * Stop group: 0, 1, 3, 7, 8
     */
    private String determineMachineStatus(int statusValue) {
        switch (statusValue) {
            // Running group
            case 2: // STaRT
            case 4: // ReSTaRt (not blinking)
            case 5: // PRSR (program restart)
            case 6: // NSRC (sequence number search)
            case 13: // HPCC (during RISC operation)
                return "running";

            // Stop group
            case 0: // STOP
            case 1: // HOLD
            case 3: // MSTR (jog mdi)
            case 7: // ReSTaRt (blinking)
            case 8: // ReSET
                return "idle";

            // Unused values (9, 10, 11, 12) - treating as stop for safety
            default:
                return "idle";
        }
    }
}