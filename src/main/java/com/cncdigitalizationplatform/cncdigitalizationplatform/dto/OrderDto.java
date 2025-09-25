package com.cncdigitalizationplatform.cncdigitalizationplatform.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String id;
    private String customerId;
    private List<String> drawingCodes;
    private String orderNumber;
    private Long orderDate;
    private Long deliveryDate;
    private Long drawingOfNumber;
    private Long drawingPartCount;
    private String productionStatus;
    private Long estimateTime;
    private Long completionDate;
    private Long updatedDeliveryDate;
    private String shippingMethod;
    private Long overdueDeliveryDate;
    private String manufacturingOrder;
    private String remarks;
    
    private String status;  
    private Long createdTime;
    private Long updatedTime;
}
