package com.cncdigitalizationplatform.cncdigitalizationplatform.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.History;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.HistoryRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;

import jakarta.annotation.PostConstruct;

@Component
public class HistorySeeder {
    
    @Autowired
    private HistoryRepository historyRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @PostConstruct
    public void seedHistories() {
        if (historyRepository.count() > 0) {
            return; // Data already exists
        }
        
        // Get existing accounts (assuming you have some accounts seeded)
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found. Please seed accounts first.");
            return;
        }
        
        // Use first 3 accounts for testing, or create more scenarios
        Account account1 = accounts.get(0);
        Account account2 = accounts.size() > 1 ? accounts.get(1) : accounts.get(0);
        Account account3 = accounts.size() > 2 ? accounts.get(2) : accounts.get(0);
        
        List<History> histories = new ArrayList<>();
        
        // Define operation types and data types for comprehensive testing
        String[] operationTypes = {"CREATE", "UPDATE", "DELETE", "LOGIN", "LOGOUT"};
        String[] dataTypes = {"MACHINE", "ACCOUNT", "DRAWING_CODE", "ROLE", "SETTING"};
        
        long baseTime = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000L;
        
        // Seed comprehensive test data
        int historyIndex = 0;
        
        // 1. Test all operation types for account1 with MACHINE data type
        for (String operationType : operationTypes) {
            histories.add(createHistory(
                account1, 
                operationType, 
                "MACHINE",
                operationType + " operation on machine by " + account1.getFullName(),
                baseTime - (historyIndex * 60000) // 1 minute intervals
            ));
            historyIndex++;
        }
        
        // 2. Test all data types for account1 with CREATE operation
        for (String dataType : dataTypes) {
            histories.add(createHistory(
                account1,
                "CREATE",
                dataType,
                "CREATE operation on " + dataType + " by " + account1.getFullName(),
                baseTime - (historyIndex * 60000)
            ));
            historyIndex++;
        }
        
        // 3. Test mixed scenarios for account2
        histories.addAll(Arrays.asList(
            createHistory(account2, "CREATE", "MACHINE", "Created new CNC machine", baseTime - (historyIndex++ * 60000)),
            createHistory(account2, "UPDATE", "MACHINE", "Updated machine status to active", baseTime - (historyIndex++ * 60000)),
            createHistory(account2, "DELETE", "DRAWING_CODE", "Deleted obsolete drawing code", baseTime - (historyIndex++ * 60000)),
            createHistory(account2, "LOGIN", "ACCOUNT", "User logged in to system", baseTime - (historyIndex++ * 60000)),
            createHistory(account2, "UPDATE", "ACCOUNT", "Updated user profile", baseTime - (historyIndex++ * 60000)),
            createHistory(account2, "CREATE", "ROLE", "Created new role: Operator", baseTime - (historyIndex++ * 60000))
        ));
        
        // 4. Test mixed scenarios for account3
        histories.addAll(Arrays.asList(
            createHistory(account3, "DELETE", "MACHINE", "Removed broken machine from inventory", baseTime - (historyIndex++ * 60000)),
            createHistory(account3, "CREATE", "DRAWING_CODE", "Added new drawing code DC-2024-001", baseTime - (historyIndex++ * 60000)),
            createHistory(account3, "UPDATE", "SETTING", "Updated system language to Vietnamese", baseTime - (historyIndex++ * 60000)),
            createHistory(account3, "LOGOUT", "ACCOUNT", "User logged out", baseTime - (historyIndex++ * 60000)),
            createHistory(account3, "UPDATE", "DRAWING_CODE", "Modified drawing code specifications", baseTime - (historyIndex++ * 60000))
        ));
        
        // 5. Add some older data for time-based filtering tests
        histories.addAll(Arrays.asList(
            createHistory(account1, "CREATE", "MACHINE", "Historical: Installed first machine", baseTime - (30 * dayInMillis)),
            createHistory(account2, "UPDATE", "ACCOUNT", "Historical: Account activation", baseTime - (15 * dayInMillis)),
            createHistory(account3, "DELETE", "DRAWING_CODE", "Historical: Cleaned up old codes", baseTime - (7 * dayInMillis))
        ));
        
        // 6. Add some future-like data (recent)
        histories.addAll(Arrays.asList(
            createHistory(account1, "LOGIN", "ACCOUNT", "Recent login - testing session", baseTime + 60000),
            createHistory(account2, "CREATE", "SETTING", "Recent: Created custom settings", baseTime + 120000),
            createHistory(account3, "UPDATE", "MACHINE", "Recent: Updated machine configuration", baseTime + 180000)
        ));
        
        // 7. Add bulk operations to test performance
        for (int i = 0; i < 20; i++) {
            Account randomAccount = accounts.get(i % accounts.size());
            String randomOperation = operationTypes[i % operationTypes.length];
            String randomDataType = dataTypes[i % dataTypes.length];
            
            histories.add(createHistory(
                randomAccount,
                randomOperation,
                randomDataType,
                "Bulk operation #" + (i + 1) + " - " + randomOperation + " on " + randomDataType,
                baseTime - (historyIndex++ * 30000) // 30 second intervals
            ));
        }
        
        // Save all histories
        historyRepository.saveAll(histories);
        
        System.out.println("Successfully seeded " + histories.size() + " history records");
        printTestScenarios();
    }
    
    private History createHistory(Account account, String operationType, String dataType, String description, long time) {
        History history = new History();
        history.setAccount(account);
        history.setOperationType(operationType);
        history.setDataType(dataType);
        history.setDescription(description);
        history.setTime(time);
        return history;
    }
    
    private void printTestScenarios() {
        System.out.println("\n=== HISTORY SEEDER TEST SCENARIOS ===");
        System.out.println("The following filter combinations can now be tested:");
        System.out.println("1. Single filters:");
        System.out.println("   - By Account ID (3 different accounts)");
        System.out.println("   - By Operation Type: CREATE, UPDATE, DELETE, LOGIN, LOGOUT");
        System.out.println("   - By Data Type: MACHINE, ACCOUNT, DRAWING_CODE, ROLE, SETTING");
        System.out.println("\n2. Double filters:");
        System.out.println("   - Account + Operation Type");
        System.out.println("   - Account + Data Type");
        System.out.println("   - Operation Type + Data Type");
        System.out.println("\n3. Triple filters:");
        System.out.println("   - Account + Operation Type + Data Type");
        System.out.println("\n4. Time-based filtering:");
        System.out.println("   - Recent data (next few minutes)");
        System.out.println("   - Current data (now)");
        System.out.println("   - Historical data (7-30 days ago)");
        System.out.println("\n5. Performance testing:");
        System.out.println("   - 20+ bulk operation records");
        System.out.println("=====================================\n");
    }
}
