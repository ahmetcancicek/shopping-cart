package com.shopping.service;

import com.shopping.model.Customer;
import com.shopping.model.User;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer save(Customer customer);
    Customer findById(Long id);
    Customer findByUser(User user);
    void deleteById(Long customerId);
}
