package com.cncdigitalizationplatform.cncdigitalizationplatform.service;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.CustomerDto;

public interface CustomerService {
    CustomerDto addCustomer(CustomerDto machineDto);

    CustomerDto updateCustomer(String id, CustomerDto machineDto);

    void deleteCustomer(String id);

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(String id);
}
