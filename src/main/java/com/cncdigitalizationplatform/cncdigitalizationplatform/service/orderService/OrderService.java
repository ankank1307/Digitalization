package com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.OrderDto;

public interface OrderService {
    OrderDto addOrder(OrderDto machineDto);

    OrderDto updateOrder(String id, OrderDto machineDto);

    void deleteOrder(String id);

    List<OrderDto> getAllOrders();

    OrderDto getOrderById(String id);
    
    List<OrderDto> getOrdersByCustomer(String customerId);
}
