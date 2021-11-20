package com.shopping.service;

import com.shopping.model.Customer;
import com.shopping.model.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer saveCustomer(Customer customer);
    Optional<Customer> findById(Long id);
}
