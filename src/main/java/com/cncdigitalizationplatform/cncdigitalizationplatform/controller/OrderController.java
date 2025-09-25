package com.cncdigitalizationplatform.cncdigitalizationplatform.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.OrderDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.OrderRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService.OrderExcelExportService;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService.OrderExcelImportService;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService.OrderExcelImportService.ImportResult;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService.OrderService;

import lombok.AllArgsConstructor;

@RequestMapping("api/orders")
@AllArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderExcelExportService orderExcelExportService;
    private final OrderExcelImportService importService;
    
    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportOrdersToExcel() {
        try {
            List<Order> orders = orderRepository.findAll();
            ByteArrayInputStream in = orderExcelExportService.exportOrdersToExcel(orders);
            
            InputStreamResource resource = new InputStreamResource(in);
            
            String filename = "orders_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/import/excel")
    public ResponseEntity<Map<String, Object>> importOrdersFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Please select a file to upload"));
            }
            
            if (!isValidExcelFile(file)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Please upload a valid Excel file (.xlsx or .xls)"));
            }
            
            // Import orders
            ImportResult result = importService.importOrdersFromExcel(file);
            
            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("Import completed successfully. Created: %d, Updated: %d, Total: %d", 
                result.getCreatedCount(), result.getUpdatedCount(), result.getTotalCount()));
            response.put("totalProcessed", result.getTotalCount());
            response.put("created", result.getCreatedCount());
            response.put("updated", result.getUpdatedCount());
            
            if (!result.getErrors().isEmpty()) {
                response.put("warnings", result.getErrors());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error reading file: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Import failed: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }
    private boolean isValidExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
            contentType.equals("application/vnd.ms-excel")
        );
    }
    
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto orderDto) {
        OrderDto order = orderService.addOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("orderId") String orderId, @RequestBody OrderDto orderDto) {
        OrderDto updatedOrder = orderService.updateOrder(orderId, orderDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("orderId") String orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomer(@PathVariable("customerId") String customerId) {
        List<OrderDto> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
