package com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.CustomerRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.DrawingCodeRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.OrderRepository;

@Service
public class OrderExcelImportService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private DrawingCodeRepository drawingCodeRepository;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("yyyy-MM-dd");
    
    public ImportResult importOrdersFromExcel(MultipartFile file) throws IOException {
        List<Order> processedOrders = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int updatedCount = 0;
        int createdCount = 0;
        
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) continue;
                
                try {
                    ImportOrderResult result = processOrderFromRow(row, rowIndex);
                    if (result.getOrder() != null) {
                        processedOrders.add(result.getOrder());
                        if (result.isUpdated()) {
                            updatedCount++;
                        } else {
                            createdCount++;
                        }
                    }
                } catch (Exception e) {
                    errors.add("Row " + (rowIndex + 1) + ": " + e.getMessage());
                }
            }
        }
        
        if (!errors.isEmpty()) {
            throw new RuntimeException("Import errors: " + String.join("; ", errors));
        }
        
        return new ImportResult(processedOrders, createdCount, updatedCount, errors);
    }
    
    private ImportOrderResult processOrderFromRow(Row row, int rowIndex) {
        String orderId = getCellValueAsString(row.getCell(0));
        
        Order existingOrder = null;
        boolean isUpdate = false;
        
        // Check if order exists by ID first, then by order number
        if (!orderId.isEmpty()) {
            existingOrder = orderRepository.findById(orderId).orElse(null);
        }
        
        Order order;
        if (existingOrder != null) {
            // Update existing order
            order = updateOrderFromRow(existingOrder, row);
            isUpdate = true;
        } else {
            // Create new order
            order = createNewOrderFromRow(row);
            isUpdate = false;
        }
        
        // Save the order
        Order savedOrder = orderRepository.save(order);
        
        return new ImportOrderResult(savedOrder, isUpdate);
    }
    
    private Order updateOrderFromRow(Order existingOrder, Row row) {        
        // Update all fields from Excel (except ID and createdTime)
        existingOrder.setOrderNumber(getCellValueAsString(row.getCell(1)));
        
        // Handle Customer (2)
        String customerName = getCellValueAsString(row.getCell(2));
        if (!customerName.isEmpty()) {
            Customer customer = findOrCreateCustomer(customerName);
            existingOrder.setCustomer(customer);
        }
        
        // Handle Drawing Codes (3)
        String drawingCodesStr = getCellValueAsString(row.getCell(3));
        if (!drawingCodesStr.isEmpty()) {
            Set<DrawingCode> drawingCodes = findOrCreateDrawingCodes(drawingCodesStr);
            existingOrder.setDrawingCodes(drawingCodes);
        } else {
            existingOrder.setDrawingCodes(new HashSet<>()); // Clear if empty
        }
        
        // Update all other fields
        existingOrder.setOrderDate(parseDate(getCellValueAsString(row.getCell(4))));
        existingOrder.setDeliveryDate(parseDate(getCellValueAsString(row.getCell(5))));
        existingOrder.setDrawingOfNumber(parseLong(getCellValueAsString(row.getCell(6))));
        existingOrder.setDrawingPartCount(parseLong(getCellValueAsString(row.getCell(7))));
        existingOrder.setProductionStatus(getCellValueAsString(row.getCell(8)));
        existingOrder.setEstimateTime(parseLong(getCellValueAsString(row.getCell(9))));
        existingOrder.setCompletionDate(parseDate(getCellValueAsString(row.getCell(10))));
        existingOrder.setUpdatedDeliveryDate(parseDate(getCellValueAsString(row.getCell(11))));
        existingOrder.setShippingMethod(getCellValueAsString(row.getCell(12)));
        existingOrder.setOverdueDeliveryDate(parseDate(getCellValueAsString(row.getCell(13))));
        existingOrder.setManufacturingOrder(getCellValueAsString(row.getCell(14)));
        existingOrder.setRemarks(getCellValueAsString(row.getCell(15)));
        existingOrder.setStatus(getCellValueAsString(row.getCell(16)));
        
        // Update timestamp for modification
        existingOrder.setUpdatedTime(System.currentTimeMillis());
        // Keep original createdTime
        
        return existingOrder;
    }
    
    private Order createNewOrderFromRow(Row row) {
        Order order = new Order();
        long currentTime = System.currentTimeMillis();
        
        // Column mapping:
        // 0: Order ID, 1: Order Number, 2: Customer Name, 3: Drawing Codes, 
        // 4: Order Date, 5: Delivery Date, 6: Drawing Of Number, 7: Drawing Part Count,
        // 8: Production Status, 9: Estimate Time, 10: Completion Date, 
        // 11: Updated Delivery Date, 12: Shipping Method, 13: Overdue Delivery Date,
        // 14: Manufacturing Order, 15: Remarks, 16: Status
        
        // Don't set ID - let it be auto-generated unless specifically provided
        String orderId = getCellValueAsString(row.getCell(0));
        if (!orderId.isEmpty()) {
            order.setId(orderId);
        }
        
        order.setOrderNumber(getCellValueAsString(row.getCell(1)));
        
        // Handle Customer (2)
        String customerName = getCellValueAsString(row.getCell(2));
        if (!customerName.isEmpty()) {
            Customer customer = findOrCreateCustomer(customerName);
            order.setCustomer(customer);
        }
        
        // Handle Drawing Codes (3)
        String drawingCodesStr = getCellValueAsString(row.getCell(3));
        if (!drawingCodesStr.isEmpty()) {
            Set<DrawingCode> drawingCodes = findOrCreateDrawingCodes(drawingCodesStr);
            order.setDrawingCodes(drawingCodes);
        }
        
        // Handle dates and other fields
        order.setOrderDate(parseDate(getCellValueAsString(row.getCell(4))));
        order.setDeliveryDate(parseDate(getCellValueAsString(row.getCell(5))));
        order.setDrawingOfNumber(parseLong(getCellValueAsString(row.getCell(6))));
        order.setDrawingPartCount(parseLong(getCellValueAsString(row.getCell(7))));
        order.setProductionStatus(getCellValueAsString(row.getCell(8)));
        order.setEstimateTime(parseLong(getCellValueAsString(row.getCell(9))));
        order.setCompletionDate(parseDate(getCellValueAsString(row.getCell(10))));
        order.setUpdatedDeliveryDate(parseDate(getCellValueAsString(row.getCell(11))));
        order.setShippingMethod(getCellValueAsString(row.getCell(12)));
        order.setOverdueDeliveryDate(parseDate(getCellValueAsString(row.getCell(13))));
        order.setManufacturingOrder(getCellValueAsString(row.getCell(14)));
        order.setRemarks(getCellValueAsString(row.getCell(15)));
        order.setStatus(getCellValueAsString(row.getCell(16)));
        
        // Set timestamps for new order
        order.setCreatedTime(currentTime);
        order.setUpdatedTime(currentTime);
        
        return order;
    }
    
    // All the existing helper methods remain the same...
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DATE_FORMAT.format(cell.getDateCellValue());
                } else {
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }
    
    private Long parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        
        try {
            Date date = DATE_FORMAT.parse(dateStr.trim());
            return date.getTime();
        } catch (ParseException e1) {
            try {
                Date date = DATE_FORMAT_SHORT.parse(dateStr.trim());
                return date.getTime();
            } catch (ParseException e2) {
                try {
                    return Long.parseLong(dateStr.trim());
                } catch (NumberFormatException e3) {
                    return null;
                }
            }
        }
    }
    
    private Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private Customer findOrCreateCustomer(String customerName) {
        Optional<Customer> existingCustomer = customerRepository.findByName(customerName);
        if (existingCustomer.isPresent()) {
            return existingCustomer.get();
        }
        
        Customer newCustomer = new Customer();
        newCustomer.setName(customerName);
        newCustomer.setStatus("ACTIVE");
        return customerRepository.save(newCustomer);
    }
    
    private Set<DrawingCode> findOrCreateDrawingCodes(String drawingCodesStr) {
        Set<DrawingCode> drawingCodes = new HashSet<>();
        String[] codeNames = drawingCodesStr.split(",");
        
        for (String codeName : codeNames) {
            String trimmedName = codeName.trim();
            if (!trimmedName.isEmpty()) {
                DrawingCode drawingCode = findOrCreateDrawingCode(trimmedName);
                drawingCodes.add(drawingCode);
            }
        }
        
        return drawingCodes;
    }
    
    private DrawingCode findOrCreateDrawingCode(String codeName) {
        Optional<DrawingCode> existingCode = drawingCodeRepository.findByName(codeName);
        if (existingCode.isPresent()) {
            return existingCode.get();
        }
        
        DrawingCode newCode = new DrawingCode();
        newCode.setName(codeName);
        newCode.setStatus("ACTIVE");
        return drawingCodeRepository.save(newCode);
    }
    
    private boolean isEmptyRow(Row row) {
        for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && !getCellValueAsString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    // Inner classes for result handling
    public static class ImportResult {
        private final List<Order> orders;
        private final int createdCount;
        private final int updatedCount;
        private final List<String> errors;
        
        public ImportResult(List<Order> orders, int createdCount, int updatedCount, List<String> errors) {
            this.orders = orders;
            this.createdCount = createdCount;
            this.updatedCount = updatedCount;
            this.errors = errors;
        }
        
        // Getters
        public List<Order> getOrders() { return orders; }
        public int getCreatedCount() { return createdCount; }
        public int getUpdatedCount() { return updatedCount; }
        public List<String> getErrors() { return errors; }
        public int getTotalCount() { return createdCount + updatedCount; }
    }
    
    private static class ImportOrderResult {
        private final Order order;
        private final boolean isUpdated;
        
        public ImportOrderResult(Order order, boolean isUpdated) {
            this.order = order;
            this.isUpdated = isUpdated;
        }
        
        public Order getOrder() { return order; }
        public boolean isUpdated() { return isUpdated; }
    }
}