package com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.TimeAnalysisDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessTime;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessTimeRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.logService.LogAnalyticsService;

@Service
public class OeeExcelService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private LogAnalyticsService logAnalyticsService;

    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ProcessTimeRepository processTimeRepository;

    public List<MachineOEEData> getAllMachinesOEEData(Long startDate, Long endDate) {
        List<MachineOEEData> machineDataList = new ArrayList<>();

        // Get all machines and process them
        List<Machine> machines = machineRepository.findAll();

        for (Machine machine : machines) {
            System.out.println("Processing machine: " + machine.getId());

            try {
                MachineOEEData oeeData = processMachineData(machine, startDate, endDate);
                machineDataList.add(oeeData);

            } catch (Exception e) {
                System.err.println("Error processing machine " + machine.getId() + ": " + e.getMessage());
                MachineOEEData errorData = createErrorData(machine);
                machineDataList.add(errorData);
            }
        }

        return machineDataList;
    }

    private MachineOEEData processMachineData(Machine machine, Long startDate, Long endDate) {
        String machineId = machine.getId();
        String machineName = machine.getName() != null ? machine.getName() : machineId;

        // Calculate OEE for this machine
        double[] oee = calculateOEEForMachine(machineId, startDate, endDate);

        // Get time analysis for this machine
        TimeAnalysisDto timeAnalysis = getTimeAnalysisForMachine(machineId, startDate, endDate);

        // Convert times to hours and round to nearest 0.5
        double runTimeHours = timeAnalysis != null ? roundToHalf(timeAnalysis.getRunTime() / 3600000.0) : 0.0;
        double stopTimeHours = timeAnalysis != null ? roundToHalf(timeAnalysis.getIdleTime() / 3600000.0) : 0.0;
        double errorTimeHours = timeAnalysis != null ? roundToHalf(timeAnalysis.getErrorTime() / 3600000.0) : 0.0;

        // Convert OEE to percentage and round to 3 decimal places
        double oeePercentage = oee[0] != 0.0 ? roundToThreeDecimalPlaces(oee[0] * 100) : 0.0;

        System.out.println("Completed processing machine: " + machineId + " - OEE: " + oeePercentage + "%");

        return new MachineOEEData(machineId, machineName, oeePercentage, runTimeHours, stopTimeHours, errorTimeHours,
                oee[1] * 100, oee[2] * 100, oee[3] * 100);
    }

    private MachineOEEData createErrorData(Machine machine) {
        String machineId = machine.getId();
        String machineName = machine.getName() != null ? machine.getName() : machineId;

        return new MachineOEEData(machineId, machineName, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    private double[] calculateOEEForMachine(String machineId, Long startDate, Long endDate) {
        double[] oee = { 0.0, 0.0, 0.0, 0.0 };
        try {
            System.out.println("Calculating OEE for machine: " + machineId);

            List<Process> processes = processRepository.findByDateRangeAndMachineId(startDate * 1000, endDate * 1000,
                    machineId);

            System.out.println("Found processes for machine " + machineId + ": " + processes.size());

            if (processes.isEmpty()) {

                return oee;
            }

            // double totalPredictedDuration = processes.stream()
            // .filter(p -> p.getPredictedDuration() != null)
            // .mapToDouble(p -> {
            // double durationInDays = p.getPredictedDuration() / (1000.0 * 60 * 60 * 24);
            // return durationInDays * (22.0 / 24.0);
            // })
            // .sum();

            double idealProductionTime = processes.stream()
                    .filter(p -> p.getPredictedDuration() != null)
                    .mapToDouble(p -> {
                        double durationInMinutes = p.getPredictedDuration() / (1000.0 * 60);
                        return durationInMinutes;
                    })
                    .sum();
            long approvedCount = processes.stream()
                    .filter(p -> "true".equals(p.getApproved()))
                    .count();

            double runtime = processes.stream()
                    .mapToDouble(p -> {
                        ProcessTime pt = processTimeRepository.findByProcess_Id(p.getId());
                        return pt != null ? pt.getRunTime() : 0.0;
                    })
                    .sum();

            double availability = runtime / (21 * 60);

            double performance = (idealProductionTime) / runtime;
            double quality = approvedCount / processes.size();
            double result = availability * performance * quality;
            oee[0] = result;
            oee[1] = availability;
            oee[2] = performance;
            oee[3] = quality;
            return oee;

        } catch (Exception e) {
            System.err.println("Error calculating OEE for machine " + machineId + ": " + e.getMessage());
            return oee;
        }
    }

    private TimeAnalysisDto getTimeAnalysisForMachine(String machineId, Long startDate, Long endDate) {
        try {
            System.out.println("Getting time analysis for machine: " + machineId);
            return logAnalyticsService.getTimeAnalysis(machineId, null, null, startDate, endDate);
        } catch (Exception e) {
            System.err.println("Error getting time analysis for machine " + machineId + ": " + e.getMessage());
            return null;
        }
    }

    // Helper method to round to nearest 0.5
    private double roundToHalf(double value) {
        return Math.round(value * 2.0) / 2.0;
    }

    // Helper method to round to 3 decimal places
    private double roundToThreeDecimalPlaces(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }

    // Data class for JSON serialization
    public static class MachineOEEData {
        private final String id;
        private final String name;
        private final Double oeePercentage;
        private final Double runTimeHours;
        private final Double stopTimeHours;
        private final Double errorTimeHours;
        private final Double availability;
        private final Double performance;
        private final Double quality;

        public MachineOEEData(String id, String name, Double oeePercentage, Double runTimeHours,
                Double stopTimeHours, Double errorTimeHours, double availablity, double performance, double quality) {
            this.id = id;
            this.name = name;
            this.oeePercentage = oeePercentage;
            this.runTimeHours = runTimeHours;
            this.stopTimeHours = stopTimeHours;
            this.errorTimeHours = errorTimeHours;
            this.availability = availablity;
            this.performance = performance;
            this.quality = quality;
        }

        // Getters for JSON serialization
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Double getOeePercentage() {
            return oeePercentage;
        }

        public Double getRunTimeHours() {
            return runTimeHours;
        }

        public Double getStopTimeHours() {
            return stopTimeHours;
        }

        public Double getErrorTimeHours() {
            return errorTimeHours;
        }

        public Double getAvailability() {
            return availability;
        }

        public Double getPerformance() {
            return performance;
        }

        public Double getQuality() {
            return quality;
        }

    }
}