package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Log;

public interface LogRepository extends JpaRepository<Log, String> {
        List<Log> findByMachine_Id(String machineId);

        List<Log> findByProcess_Id(String processId);

        List<Log> findByAccount_Id(String accountId);

        // Find logs by machine within time range
        List<Log> findByMachineIdAndTimeStampBetweenOrderByTimeStampAsc(
                        String machineId, Long startTime, Long endTime);

        // Find logs by account within time range
        List<Log> findByAccountIdAndTimeStampBetweenOrderByTimeStampAsc(
                        String accountId, Long startTime, Long endTime);

        // Find logs by process within time range
        List<Log> findByProcessIdAndTimeStampBetweenOrderByTimeStampAsc(
                        String processId, Long startTime, Long endTime);

        // Find logs with multiple filters
        @Query("SELECT l FROM Log l WHERE " +
                        "(:machineId IS NULL OR l.machine.id = :machineId) AND " +
                        "(:accountId IS NULL OR l.account.id = :accountId) AND " +
                        "(:processId IS NULL OR l.process.id = :processId) AND " +
                        "(:startTime IS NULL OR l.timeStamp >= :startTime) AND " +
                        "(:endTime IS NULL OR l.timeStamp <= :endTime) " +
                        "ORDER BY l.timeStamp ASC")
        List<Log> findLogsWithFilters(
                        @Param("machineId") String machineId,
                        @Param("accountId") String accountId,
                        @Param("processId") String processId,
                        @Param("startTime") Long startTime,
                        @Param("endTime") Long endTime);

        // Add this to LogRepository interface for better performance
        Optional<Log> findFirstByMachine_IdOrderByTimeStampDesc(String machineId);

        @Query(value = """
                        WITH OrderedEvents AS (
                            SELECT
                                machine_id,
                                machine_status,
                                time_stamp,
                                LEAD(time_stamp) OVER (PARTITION BY machine_id ORDER BY time_stamp) AS next_time
                            FROM log
                            WHERE machine_id = :machineId
                              AND time_stamp BETWEEN :startDate AND :endDate
                        )
                        SELECT
                            machine_id,
                            SUM(
                                CASE
                                    WHEN machine_status = 'running' AND next_time IS NOT NULL
                                    THEN DATEDIFF(
                                             SECOND,
                                             DATEADD(MILLISECOND, time_stamp % 1000, DATEADD(SECOND, time_stamp / 1000, '1970-01-01')),
                                             DATEADD(MILLISECOND, next_time % 1000, DATEADD(SECOND, next_time / 1000, '1970-01-01'))
                                         ) / 60.0
                                    ELSE 0
                                END
                            ) AS running_time_minutes
                        FROM OrderedEvents
                        GROUP BY machine_id
                        """, nativeQuery = true)
        Double getRunningTimeByMachine(
                        @Param("machineId") String machineId,
                        @Param("startDate") long startDate,
                        @Param("endDate") long endDate);

        @Query(value = """
                        WITH OrderedEvents AS (
                            SELECT
                                process_id,
                                machine_status,
                                time_stamp,
                                LEAD(time_stamp) OVER (PARTITION BY process_id ORDER BY time_stamp) AS next_time
                            FROM log
                            WHERE process_id = :processId
                        )
                        SELECT
                            SUM(
                                CASE
                                    WHEN machine_status = 'running' AND next_time IS NOT NULL
                                    THEN (next_time - time_stamp) / 60000.0
                                    ELSE 0
                                END
                            ) AS runTime,
                            SUM(
                                CASE
                                    WHEN machine_status = 'idle' AND next_time IS NOT NULL
                                    THEN (next_time - time_stamp) / 60000.0
                                    ELSE 0
                                END
                            ) AS stopTime
                        FROM OrderedEvents
                        """, nativeQuery = true)
        Object calculateRunningAndIdleTimeByProcess(@Param("processId") String processId);
}
