package com.cncdigitalizationplatform.cncdigitalizationplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.CustomerDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.CustomerService;

import lombok.AllArgsConstructor;

@RequestMapping("api/customers")
@AllArgsConstructor
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto customer = customerService.addCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("customerId") String customerId, @RequestBody CustomerDto customerDto) {
        CustomerDto updatedCustomer = customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customerId") String customerId) {
        CustomerDto customer = customerService.getCustomerById(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
