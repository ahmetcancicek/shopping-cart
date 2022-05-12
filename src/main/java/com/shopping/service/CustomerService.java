package com.shopping.service;

import com.shopping.dto.RegistrationRequest;
import com.shopping.dto.CustomerResponse;
import com.shopping.model.Customer;
import com.shopping.model.User;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> findAll();
    CustomerResponse save(RegistrationRequest customer);
    CustomerResponse findById(Long id);
    CustomerResponse findByUser(User user);
    CustomerResponse findByUsername(String username);
    CustomerResponse findByEmail(String email);
    Customer findCustomerByUsername(String username);
    void deleteById(Long id);
    void deleteByUsername(String username);
}
