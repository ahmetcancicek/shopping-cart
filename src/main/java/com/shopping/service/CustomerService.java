package com.shopping.service;

import com.shopping.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
}
