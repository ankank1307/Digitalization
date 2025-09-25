package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.CustomerDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.CustomerMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.CustomerRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.CustomerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImplementation implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerDto addCustomer(CustomerDto customerDto) {
        if (customerDto.getName() == null) {
            throw new IllegalArgumentException("name");
        }
        
        if (customerDto.getPhoneNumber() == null && customerDto.getEmail() == null) {
            throw new IllegalArgumentException("phoneEmail");
        }
        
        Customer customer = customerMapper.toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerMapper.toDtoList(customerRepository.findAll());
    }

    @Override
    public CustomerDto getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("customer"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("customer"));
        
        customerMapper.updateCustomerFromDto(customerDto, existingCustomer);
        existingCustomer.setUpdatedTime(System.currentTimeMillis());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("customer"));
        customerRepository.delete(customer);
    }
}

