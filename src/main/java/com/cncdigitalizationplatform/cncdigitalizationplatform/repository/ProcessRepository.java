package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;

public interface ProcessRepository extends JpaRepository<Process, String> {

       Optional<Process> findByMachine_IdAndTaskStatus(String machineId, String taskStatus);

       // Existing method
       @Query("SELECT p FROM Process p WHERE " +
                     "p.startTime IS NOT NULL AND p.predictedDuration IS NOT NULL AND " +
                     "(p.startTime + p.predictedDuration) >= :startOfDay AND " +
                     "(p.startTime + p.predictedDuration) < :endOfDay")
       List<Process> findByPredictedEndDateRange(@Param("startOfDay") Long startOfDay,
                     @Param("endOfDay") Long endOfDay);

       // Updated method - uses exact startDate and endDate without conversion
       @Query(value = "SELECT * FROM process p " +
                     "WHERE p.machine_id = :machineId " +
                     "AND p.end_time IS NOT NULL " +
                     "AND p.end_time BETWEEN :startDate AND :endDate", nativeQuery = true)
       List<Process> findByDateRangeAndMachineId(@Param("startDate") Long startDate,
                     @Param("endDate") Long endDate,
                     @Param("machineId") String machineId);

       // Alternative: Find processes that are running/overlapping within the time
       // range
       @Query("SELECT p FROM Process p WHERE " +
                     "p.machine.id = :machineId AND " +
                     "p.startTime IS NOT NULL AND p.predictedDuration IS NOT NULL AND " +
                     "p.startTime <= :endDate AND " +
                     "(p.startTime + p.predictedDuration) >= :startDate")
       List<Process> findByOverlappingDateRangeAndMachineId(@Param("startDate") Long startDate,
                     @Param("endDate") Long endDate,
                     @Param("machineId") String machineId);
}