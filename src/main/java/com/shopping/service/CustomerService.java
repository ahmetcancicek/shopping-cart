package com.shopping.service;

import com.shopping.dto.CustomerRequest;
import com.shopping.dto.CustomerResponse;
import com.shopping.model.Customer;
import com.shopping.model.User;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> findAll();
    CustomerResponse save(CustomerRequest customer);
    CustomerResponse findById(Long id);
    CustomerResponse findByUser(User user);

    CustomerResponse findByUsername(String username);

    CustomerResponse findByEmail(String email);

    Customer findCustomerByUsername(String username);

    void deleteById(Long id);

    void deleteByUsername(String username);
}
