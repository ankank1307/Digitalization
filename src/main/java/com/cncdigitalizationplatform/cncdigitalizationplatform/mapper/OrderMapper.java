package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.OrderDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    
    /**
     * Maps Order entity to OrderDto
     * Custom mapping for customer ID and drawing code IDs
     */
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(target = "drawingCodes", expression = "java(mapDrawingCodesToIds(order.getDrawingCodes()))")
    OrderDto toDto(Order order);
    
    /**
     * Maps OrderDto to Order entity with relationship parameters
     */
    @Mapping(source = "orderDto.id", target = "id")
    @Mapping(source = "orderDto.status", target = "status")
    @Mapping(source = "customer", target = "customer")
    @Mapping(source = "drawingCodeEntities", target = "drawingCodes")
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Order toEntity(OrderDto orderDto, Customer customer, Set<DrawingCode> drawingCodeEntities);
    
    /**
     * Maps list of Order entities to list of OrderDtos
     */
    List<OrderDto> toDtoList(List<Order> orders);
    
    /**
     * Updates existing Order entity with data from OrderDto
     * Ignores ID, relationships, and timestamp fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "drawingCodes", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateOrderFromDto(OrderDto orderDto, @MappingTarget Order order);
    
    /**
     * Helper method to convert DrawingCode entities to ID list
     */
    default List<String> mapDrawingCodesToIds(Set<DrawingCode> drawingCodes) {
        if (drawingCodes == null) {
            return null;
        }
        return drawingCodes.stream()
                          .map(DrawingCode::getId)
                          .collect(Collectors.toList());
    }
}