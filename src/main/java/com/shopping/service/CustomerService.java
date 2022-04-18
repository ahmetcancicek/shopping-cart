package com.shopping.service;

import com.shopping.model.Customer;
import com.shopping.model.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    Optional<Customer> findByUser(User user);
    void deleteById(Long customerId);
}
