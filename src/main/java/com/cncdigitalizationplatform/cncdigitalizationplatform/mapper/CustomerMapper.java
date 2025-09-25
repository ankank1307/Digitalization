package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.CustomerDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerMapper {
    
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    
    /**
     * Maps Customer entity to CustomerDto
     */
    CustomerDto toDto(Customer customer);
    
    /**
     * Maps CustomerDto to Customer entity
     * Ignores timestamp fields as they are managed by the database
     */
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Customer toEntity(CustomerDto customerDto);
    
    /**
     * Maps list of Customer entities to list of CustomerDtos
     */
    List<CustomerDto> toDtoList(List<Customer> customers);
    
    /**
     * Updates existing Customer entity with data from CustomerDto
     * Ignores ID and timestamp fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer);
}