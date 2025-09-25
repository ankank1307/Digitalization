package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.OrderDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.OrderMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.CustomerRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.DrawingCodeRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.OrderRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.orderService.OrderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DrawingCodeRepository drawingCodeRepository;
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderDto addOrder(OrderDto orderDto) {
        // Fetch customer
        Customer customer = null;
        if (orderDto.getCustomerId() != null) {
            customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("customer"));
        } else {
            throw new IllegalArgumentException("customer");
        }

        // Fetch drawing codes
        Set<DrawingCode> drawingCodes = new HashSet<>();
        if (orderDto.getDrawingCodes() != null) {
            for (String drawingId : orderDto.getDrawingCodes()) {
                DrawingCode drawing = drawingCodeRepository.findById(drawingId)
                    .orElseThrow(() -> new ResourceNotFoundException("drawing"));
                drawingCodes.add(drawing);
            }
        } else {
            throw new IllegalArgumentException("drawing");
        }

        Order order = orderMapper.toEntity(orderDto, customer, drawingCodes);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public OrderDto getOrderById(String id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("order"));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto updateOrder(String id, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("order"));

        orderMapper.updateOrderFromDto(orderDto, existingOrder);

        // Update customer
        if (orderDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("customer"));
            existingOrder.setCustomer(customer);
        }

        // Update drawing codes
        if (orderDto.getDrawingCodes() != null) {
            Set<DrawingCode> drawingCodes = new HashSet<>();
            for (String drawingId : orderDto.getDrawingCodes()) {
                DrawingCode drawing = drawingCodeRepository.findById(drawingId)
                    .orElseThrow(() -> new ResourceNotFoundException("drawing"));
                drawingCodes.add(drawing);
            }
            existingOrder.setDrawingCodes(drawingCodes);
        }
        
        existingOrder.setUpdatedTime(System.currentTimeMillis());

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("order"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersByCustomer(String customerId) {
        List<Order> orders = orderRepository.findByCustomer_Id(customerId);
        return orderMapper.toDtoList(orders);
    }
}
