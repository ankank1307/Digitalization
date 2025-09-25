package com.cncdigitalizationplatform.cncdigitalizationplatform.service.Schedulers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessOperator;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessOperatorRepository;

@Service
public class ProcessOperatorSchedulerService {
    
    @Autowired
    ProcessOperatorRepository processOperatorRepository;

    // Runs at 7:00 AM and 7:00 PM every day
    @Scheduled(cron = "0 0 7,19 * * *")
    public void createNewProcessOperatorEntries() {
        System.out.println("Running scheduled task to create new ProcessOperator entries...");
        
        // Calculate timestamp for 12 hours ago
        long twelveHoursAgo = System.currentTimeMillis() - (12 * 60 * 60 * 1000);
        
        // Find all ProcessOperators created within last 12 hours that have an assigned operator
        List<ProcessOperator> recentAssignedOperators = processOperatorRepository.findByAccountIsNotNullAndCreatedTimeGreaterThan(twelveHoursAgo);
        
        for (ProcessOperator existingEntry : recentAssignedOperators) {
            // Check if process taskStatus is "in_progress"
            if (!"in_progress".equals(existingEntry.getProcess().getTaskStatus())) {
                continue; // Skip this process if not in progress
            }
            
            // Get the latest entry for this process
            ProcessOperator latestEntry = processOperatorRepository.findTopByProcessOrderByCreatedTimeDesc(existingEntry.getProcess());
            
            if (latestEntry.getAccount() != null) {
                ProcessOperator newEntry = new ProcessOperator();
                newEntry.setProcess(existingEntry.getProcess());
                newEntry.setAccount(null);
                newEntry.setCreatedTime(System.currentTimeMillis());
                
                processOperatorRepository.save(newEntry);
                System.out.println("Created new unassigned entry for process: " + existingEntry.getProcess().getId());
            } else {
                System.out.println("Latest entry for process " + existingEntry.getProcess().getId() + " is already unassigned. Skipping.");
            }
        }
        
        System.out.println("Scheduled task completed.");
    }
}