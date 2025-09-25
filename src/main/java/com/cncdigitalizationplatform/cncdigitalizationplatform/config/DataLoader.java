package com.cncdigitalizationplatform.cncdigitalizationplatform.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Log;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessOperator;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.AccountSetting;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Role;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.CustomerRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.DrawingCodeRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.LogRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.OrderRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessOperatorRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.ProcessRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountSettingRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader {

        private final RoleRepository roleRepository;
        private final AccountRepository accountRepository;
        private final AccountSettingRepository accountSettingRepository;
        private final CustomerRepository customerRepository;
        private final DrawingCodeRepository drawingCodeRepository;
        private final MachineRepository machineRepository;
        private final OrderRepository orderRepository;
        private final ProcessRepository processRepository;
        private final LogRepository logRepository;
        private final ProcessOperatorRepository processOperatorRepository;

        @Autowired
        private HistorySeeder historySeeder;

        @PostConstruct
        public void loadData() {
                // Create roles

                if (roleRepository.count() > 0 ||
                                accountRepository.count() > 0 ||
                                customerRepository.count() > 0 ||
                                drawingCodeRepository.count() > 0) {
                        System.out.println("🟡 Skipping DataLoader: data already exists.");
                        return;
                }

                System.out.println("🟢 Running DataLoader...");
                Role operatorRole = new Role();
                operatorRole.setRoleName("operator");
                operatorRole.setRoleShort("op");
                roleRepository.save(operatorRole);

                Role qcRole = new Role();
                qcRole.setRoleName("quality control");
                qcRole.setRoleShort("qc");
                roleRepository.save(qcRole);

                Role adminRole = new Role();
                adminRole.setRoleName("admin");
                adminRole.setRoleShort("admin");
                roleRepository.save(adminRole);

                // Create accounts
                Account operator1 = new Account();
                operator1.setFullName("Operator One");
                operator1.setEmail("operatorone@gmail.com");
                operator1.setPhoneNumber("0754352732");
                operator1.setRole(operatorRole);
                operator1.setAvatarUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr"); // modify
                                                                                                                        // later
                accountRepository.save(operator1);

                Account operator2 = new Account();
                operator2.setFullName("Operator Two");
                operator2.setEmail("operatortwo@gmail.com");
                operator2.setPhoneNumber("0963621524");
                operator2.setRole(operatorRole);
                operator2.setStatus("inactive");
                operator2.setAvatarUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");// modify
                                                                                                                       // later
                accountRepository.save(operator2);

                Account qc = new Account();
                qc.setFullName("QC User");
                qc.setEmail("qc@gmail.com");
                qc.setPhoneNumber("0931367423");
                qc.setRole(qcRole);
                qc.setAvatarUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");// modify
                                                                                                                // later
                accountRepository.save(qc);

                Account admin = new Account();
                admin.setFullName("Admin Line 1");
                admin.setEmail("adminline1@gmail.com");
                admin.setPhoneNumber("0658358248");
                admin.setRole(adminRole);
                admin.setAvatarUrl(
                                "https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");
                accountRepository.save(admin);

                Account piao = new Account();
                piao.setFullName("Admin Line 2");
                piao.setEmail("adminline2@gmail.com");
                piao.setPhoneNumber("0425674235");
                piao.setRole(adminRole);
                piao.setAvatarUrl(
                                "https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");
                accountRepository.save(piao);

                Account huy = new Account();
                huy.setFullName("Admin Line 3");
                huy.setEmail("adminline3@gmail.com");
                huy.setPhoneNumber("0399877687");
                huy.setRole(adminRole);
                huy.setAvatarUrl(
                                "https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");
                accountRepository.save(huy);

                Account gao = new Account();
                gao.setFullName("Operator Three");
                gao.setEmail("operatorthree@gmail.com");
                gao.setPhoneNumber("0983452834");
                gao.setRole(operatorRole);
                gao.setAvatarUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");
                accountRepository.save(gao);

                Account giap = new Account();
                giap.setFullName("Operator Four");
                giap.setEmail("operatorfour@gmail.com");
                giap.setPhoneNumber("0414183314");
                giap.setRole(operatorRole);
                giap.setAvatarUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");
                accountRepository.save(giap);

                Account phuong = new Account();
                phuong.setFullName("Operator Five");
                phuong.setEmail("operatorfive@gmail.com");
                phuong.setPhoneNumber("0342635178");
                phuong.setRole(operatorRole);
                phuong.setAvatarUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXCkkR3xMm5lw1kMVTfoSYjORvPU7WzpDdAXsr");
                accountRepository.save(phuong);

                // Create account settings
                accountSettingRepository
                                .save(new AccountSetting(null, admin, "system", "cn", "active",
                                                System.currentTimeMillis(), null));
                accountSettingRepository
                                .save(new AccountSetting(null, operator1, "light", "en", "active",
                                                System.currentTimeMillis(), null));
                accountSettingRepository
                                .save(new AccountSetting(null, operator2, "light", "en", "active",
                                                System.currentTimeMillis(), null));
                accountSettingRepository
                                .save(new AccountSetting(null, qc, "light", "en", "active", System.currentTimeMillis(),
                                                null));
                accountSettingRepository
                                .save(new AccountSetting(null, huy, "light", "en", "active", System.currentTimeMillis(),
                                                null));
                accountSettingRepository
                                .save(new AccountSetting(null, piao, "system", "cn", "active",
                                                System.currentTimeMillis(), null));

                // Create customers
                Customer customer1 = new Customer();
                customer1.setName("Customer A");
                customer1.setEmail("custA@example.com");
                customer1.setPhoneNumber("11111111");
                customerRepository.save(customer1);

                Customer customer2 = new Customer();
                customer2.setName("Customer B");
                customer2.setEmail("custB@example.com");
                customer2.setPhoneNumber("22222222");
                customerRepository.save(customer2);

                // Create and save drawing codes
                DrawingCode drawing1 = new DrawingCode();
                drawing1.setName("Drawing A");
                drawing1.setPdfUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXywLLlGVO5c6f2RKekrEt489jlzwsPDyZL13n");
                drawing1.setDescription("Cutting");
                drawingCodeRepository.save(drawing1);

                DrawingCode drawing2 = new DrawingCode();
                drawing2.setName("Drawing B");
                drawing2.setPdfUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQX1WXmDnFYiBMIhE3b8m5CyfurkKDR0coxVtXp");
                drawing2.setDescription("Cutting");
                drawingCodeRepository.save(drawing2);

                DrawingCode drawing3 = new DrawingCode();
                drawing3.setName("Drawing C");
                drawing3.setPdfUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXhrxZDZ6xkCd5VNQjbHv9lY6t0mBUgy3AcZfS");
                drawing3.setDescription("Cutting");
                drawingCodeRepository.save(drawing3);

                DrawingCode drawing4 = new DrawingCode();
                drawing4.setName("Drawing D");
                drawing4.setPdfUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXk3aPJr7BruklgNnyejOALFYG8wdh0SpoT9Ub");
                drawing4.setDescription("Main assembly diagram");
                drawingCodeRepository.save(drawing4);

                DrawingCode drawing5 = new DrawingCode();
                drawing5.setName("Drawing E");
                drawing5.setPdfUrl("https://p1yiu74ub0.ufs.sh/f/vN2x7UHbkIQXnLHAV8k39wDNgzaJO7fULyGpMVFcY05RlTqe");
                drawing5.setDescription("Electrical wiring layout");
                drawingCodeRepository.save(drawing5);

                DrawingCode drawing6 = new DrawingCode();
                drawing6.setName("Drawing F");
                drawing6.setPdfUrl("mechanical_spec");
                drawing6.setDescription("Component specifications");
                drawingCodeRepository.save(drawing6);

                DrawingCode drawing7 = new DrawingCode();
                drawing7.setName("Drawing G");
                drawing7.setPdfUrl("flow_diagram");
                drawing7.setDescription("Process flow chart");
                drawingCodeRepository.save(drawing7);

                DrawingCode drawing8 = new DrawingCode();
                drawing8.setName("Drawing H");
                drawing8.setPdfUrl("layout_plan");
                drawing8.setDescription("Floor plan layout");
                drawingCodeRepository.save(drawing8);

                DrawingCode drawing9 = new DrawingCode();
                drawing9.setName("Drawing I");
                drawing9.setPdfUrl("detail_view");
                drawing9.setDescription("Cross-section details");
                drawingCodeRepository.save(drawing9);

                DrawingCode drawing10 = new DrawingCode();
                drawing10.setName("Drawing J");
                drawing10.setPdfUrl("assembly_guide");
                drawing10.setDescription("Step-by-step assembly");
                drawingCodeRepository.save(drawing10);

                DrawingCode drawing11 = new DrawingCode();
                drawing11.setName("Drawing K");
                drawing11.setPdfUrl("maintenance_doc");
                drawing11.setDescription("Maintenance procedures");
                drawingCodeRepository.save(drawing11);

                DrawingCode drawing12 = new DrawingCode();
                drawing12.setName("Drawing L");
                drawing12.setPdfUrl("safety_manual");
                drawing12.setDescription("Safety guidelines and warnings");
                drawingCodeRepository.save(drawing12);

                // Re-fetch managed DrawingCodes from DB
                drawing1 = drawingCodeRepository.findById(drawing1.getId()).orElseThrow();
                drawing2 = drawingCodeRepository.findById(drawing2.getId()).orElseThrow();
                drawing3 = drawingCodeRepository.findById(drawing3.getId()).orElseThrow();

                // Create machines
                Machine machine1 = new Machine();
                machine1.setIsActive(1);
                machine1.setName("Lathe");
                machine1.setImageUrl("https://utfs.io/f/vN2x7UHbkIQXuCZrmslsJrqx9bhtzRTXgfPUmiNcYGE4BpHl");
                machineRepository.save(machine1);

                Machine machine2 = new Machine();
                machine2.setIsActive(1);
                machine2.setName("Doosan 1");
                machine2.setImageUrl("https://utfs.io/f/vN2x7UHbkIQX64cLPRDhDxNuSlos3Of9qiWUZtRGnzBgK5kc");
                machineRepository.save(machine2);

                Machine machine3 = new Machine();
                machine3.setIsActive(1);
                machine3.setName("Doosan 2");
                machine3.setImageUrl("https://utfs.io/f/vN2x7UHbkIQXGKZP7qXjT8B0s4JulnfIc5Rq3AEiHWCxMSXv");
                machineRepository.save(machine3);

                // Create orders with managed DrawingCodes
                Order order1 = new Order();
                order1.setCustomer(customer1);
                order1.setOrderNumber("PO1234");
                order1.setOrderDate(System.currentTimeMillis());
                order1.setDeliveryDate(System.currentTimeMillis() + 86400000);
                order1.setShippingMethod("123 Street");
                order1.setDrawingCodes(new HashSet<>(Arrays.asList(drawing1, drawing2)));
                orderRepository.save(order1);

                Order order2 = new Order();
                order2.setCustomer(customer2);
                order2.setOrderNumber("PO5678");
                order2.setOrderDate(System.currentTimeMillis());
                order2.setDeliveryDate(System.currentTimeMillis() + 172800000);
                order2.setShippingMethod("456 Road");
                order2.setDrawingCodes(new HashSet<>(List.of(drawing3)));
                orderRepository.save(order2);

                // Create processes with operators and drawing codes
                Process process1 = new Process();
                process1.setDrawingCode(drawing1);
                process1.setMachine(null);
                process1.setName("Step 1");
                process1.setTaskStatus("to_do");
                // process1.setStartTime(null); // Started 1 hour ago
                process1.setPredictedDuration(360L); // 1 hour duration
                // process1.setApproved(null);
                processRepository.save(process1);

                Process process2 = new Process();
                process2.setDrawingCode(drawing2);
                process2.setMachine(null);
                process2.setName("Step 2");
                process2.setTaskStatus("to_do");
                // process2.setStartTime(null); // Started 2 hours ago
                process2.setPredictedDuration(720L); // 2 hour duration
                // process2.setApproved(null); // This will NOT be counted as approved
                processRepository.save(process2);

                // Add more processes that end today for better OEE calculation
                Process process3 = new Process();
                process3.setDrawingCode(drawing1);
                process3.setMachine(null);
                process3.setName("Step 3");
                process3.setTaskStatus("to_do");
                //// Started 30 min ago
                process3.setPredictedDuration(1800000L); // 30 min duration (ends now)
                // process3.setApproved("true"); // Approved
                // process3.setQcRemarks("good");
                // process3.setEndTime(System.currentTimeMillis());
                processRepository.save(process3);

                Process process4 = new Process();
                process4.setDrawingCode(drawing2);
                // process4.setMachine(machine1);
                process4.setName("Step 4");
                process4.setTaskStatus("to_do");
                // process4.setStartTime(System.currentTimeMillis() - 5400000L); // Started 1.5
                // hours ago
                process4.setPredictedDuration(540L); // 1.5 hour duration (ends now)
                processRepository.save(process4);

                Process process5 = new Process();
                process5.setDrawingCode(drawing1);
                // process5.setMachine(machine1);
                process5.setName("Step 5");
                process5.setTaskStatus("to_do");
                // process5.setStartTime(System.currentTimeMillis() - 2700000L); // Started 45
                // min ago
                process5.setPredictedDuration(270L); // 45 min duration (ends now)
                // process5.setApproved("false"); // Not approved (not "OK")
                // process5.setQcRemarks("horrible");
                processRepository.save(process5);

                // Add a process that ends tomorrow (should NOT be included in today's OEE)
                Process process6 = new Process();
                process6.setDrawingCode(drawing2);
                // process6.setMachine(machine1);
                process6.setName("Step 6");
                process6.setTaskStatus("to_do");
                // process6.setStartTime(System.currentTimeMillis());
                process6.setPredictedDuration(180L);
                // process6.setApproved(null);
                processRepository.save(process6);

                Process process7 = new Process();
                process7.setDrawingCode(drawing3);
                // process7.setMachine(machine1);
                process7.setName("Step 7");
                process7.setTaskStatus("to_do");
                // process7.setStartTime(System.currentTimeMillis() - 7200000L); // Started 2
                // hours ago
                process7.setPredictedDuration(360L); // 1 hour duration (completed 1 hour ago)
                // process7.setApproved(null);
                processRepository.save(process7);

                Process process8 = new Process();
                process8.setDrawingCode(drawing4);
                // process8.setMachine(machine1);
                process8.setName("Step 8");
                process8.setTaskStatus("to_do");
                // process8.setStartTime(System.currentTimeMillis());
                process8.setPredictedDuration(310L); // 1 year
                // process8.setApproved(null);
                processRepository.save(process8);

                Process process9 = new Process();
                process9.setDrawingCode(drawing5);
                // process9.setMachine(machine1);
                process9.setName("Step 9");
                process9.setTaskStatus("to_do");
                // process9.setStartTime(System.currentTimeMillis() - 10800000L); // Started 3
                // hours ago
                process9.setPredictedDuration(720L); // 2 hour duration (completed 1 hour ago)
                // process9.setApproved("true");
                // process9.setEndTime(System.currentTimeMillis());
                processRepository.save(process9);

                Process process10 = new Process();
                process10.setDrawingCode(drawing6);
                // process10.setMachine(machine1);
                process10.setName("Step 10");
                process10.setTaskStatus("to_do");
                // process10.setStartTime(System.currentTimeMillis() - 5400000L); // Started 1.5
                // hours ago
                process10.setPredictedDuration(180L); // 30 min duration (completed 1 hour ago)
                // process10.setApproved(null);
                processRepository.save(process10);

                Process process11 = new Process();
                process11.setDrawingCode(drawing7);
                // process11.setMachine(machine1);
                process11.setName("Step 11");
                process11.setTaskStatus("to_do");
                // process11.setStartTime(System.currentTimeMillis() - 900000L); // Started 15
                // min ago
                process11.setPredictedDuration(540L); // 1.5 hour duration (1 hour 15 min remaining)
                // process11.setApproved(null);
                processRepository.save(process11);

                Process process12 = new Process();
                process12.setDrawingCode(drawing8);
                // process12.setMachine(machine1);
                process12.setName("Step 12");
                process12.setTaskStatus("to_do");
                // process12.setStartTime(System.currentTimeMillis() - 14400000L); // Started 4
                // hours ago
                process12.setPredictedDuration(180L); // 3 hour duration (completed 1 hour ago)
                // process12.setApproved(null);
                processRepository.save(process12);

                // Process Operator
                // ProcessOperator processOperator1 = new ProcessOperator();
                // // processOperator1.setAccount(operator1);
                // processOperator1.setProcess(process1);
                // processOperatorRepository.save(processOperator1);

                // ProcessOperator processOperator2 = new ProcessOperator();
                // processOperator2.setAccount(phuong);
                // processOperator2.setProcess(process2);
                // processOperatorRepository.save(processOperator2);

                // Create log entries
                // Log log1 = new Log();
                // log1.setMachine(machine1);
                // log1.setAccount(operator1);
                // log1.setProcess(process1);
                // log1.setMachineStatus("running");
                // logRepository.save(log1);

                // Log log2 = new Log();
                // log2.setMachine(machine2);
                // log2.setAccount(operator1);
                // log2.setProcess(process2);
                // log2.setMachineStatus("idle");
                // logRepository.save(log2);

                // Log log3 = new Log();
                // log3.setMachine(machine1);
                // log3.setAccount(operator2);
                // log3.setProcess(process1);
                // log3.setMachineStatus("error");
                // logRepository.save(log3);

                historySeeder.seedHistories();
        }
}