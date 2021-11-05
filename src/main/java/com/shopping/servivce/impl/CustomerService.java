package com.shopping.servivce.impl;

import com.shopping.model.Customer;
import com.shopping.model.User;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer saveCustomer(Customer customer);
}
