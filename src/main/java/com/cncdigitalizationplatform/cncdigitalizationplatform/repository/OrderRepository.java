package com.cncdigitalizationplatform.cncdigitalizationplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Order;

public interface OrderRepository extends JpaRepository<Order, String>{
    List<Order> findByCustomer_Id(String customerId);
}
