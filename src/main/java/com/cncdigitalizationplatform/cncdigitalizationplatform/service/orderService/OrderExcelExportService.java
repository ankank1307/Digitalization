package com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;

@Service
public class OrderExcelExportService {
    
    private static final String EMPTY_VALUE = "";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public ByteArrayInputStream exportOrdersToExcel(List<Order> orders) throws IOException {
        String[] headers = {
            "Order ID", "Order Number", "Customer Name", "Drawing Codes", 
            "Order Date", "Delivery Date", "Drawing Of Number", "Drawing Part Count",
            "Production Status", "Estimate Time", "Completion Date", 
            "Updated Delivery Date", "Shipping Method", "Overdue Delivery Date",
            "Manufacturing Order", "Remarks", "Status", "Created Time", "Updated Time"
        };
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Orders");
            
            // Create header row with styling
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Order order : Optional.ofNullable(orders).orElse(List.of())) {
                if (order == null) continue;
                
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                
                row.createCell(colNum++).setCellValue(safeString(order.getId()));
                row.createCell(colNum++).setCellValue(safeString(order.getOrderNumber()));
                row.createCell(colNum++).setCellValue(getCustomerName(order.getCustomer()));
                row.createCell(colNum++).setCellValue(formatDrawingCodes(order.getDrawingCodes()));
                row.createCell(colNum++).setCellValue(formatDate(order.getOrderDate()));
                row.createCell(colNum++).setCellValue(formatDate(order.getDeliveryDate()));
                row.createCell(colNum++).setCellValue(safeLong(order.getDrawingOfNumber()));
                row.createCell(colNum++).setCellValue(safeLong(order.getDrawingPartCount()));
                row.createCell(colNum++).setCellValue(safeString(order.getProductionStatus()));
                row.createCell(colNum++).setCellValue(safeLong(order.getEstimateTime()));
                row.createCell(colNum++).setCellValue(formatDate(order.getCompletionDate()));
                row.createCell(colNum++).setCellValue(formatDate(order.getUpdatedDeliveryDate()));
                row.createCell(colNum++).setCellValue(safeString(order.getShippingMethod()));
                row.createCell(colNum++).setCellValue(formatDate(order.getOverdueDeliveryDate()));
                row.createCell(colNum++).setCellValue(safeString(order.getManufacturingOrder()));
                row.createCell(colNum++).setCellValue(safeString(order.getRemarks()));
                row.createCell(colNum++).setCellValue(safeString(order.getStatus()));
                row.createCell(colNum++).setCellValue(formatDate(order.getCreatedTime()));
                row.createCell(colNum++).setCellValue(formatDate(order.getUpdatedTime()));
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    
    // Utility methods for null safety
    private String safeString(String value) {
        return Optional.ofNullable(value).orElse(EMPTY_VALUE);
    }
    
    private String safeLong(Long value) {
        return Optional.ofNullable(value).map(String::valueOf).orElse(EMPTY_VALUE);
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    private String getCustomerName(Customer customer) {
        return Optional.ofNullable(customer)
                .map(Customer::getName)
                .orElse(EMPTY_VALUE);
    }
    
    private String formatDrawingCodes(Set<DrawingCode> drawingCodes) {
        return Optional.ofNullable(drawingCodes)
                .filter(codes -> !codes.isEmpty())
                .map(codes -> codes.stream()
                        .filter(dc -> dc != null)
                        .map(dc -> Optional.ofNullable(dc.getName()).orElse("Unknown"))
                        .collect(Collectors.joining(", ")))
                .orElse(EMPTY_VALUE);
    }
    
    private String formatDate(Long timestamp) {
        return Optional.ofNullable(timestamp)
                .map(ts -> {
                    try {
                        return DATE_FORMAT.format(new Date(ts));
                    } catch (Exception e) {
                        return "Invalid Date";
                    }
                })
                .orElse(EMPTY_VALUE);
    }
}