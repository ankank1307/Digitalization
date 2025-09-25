package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>{
    Optional<Customer> findByName(String name);
}
