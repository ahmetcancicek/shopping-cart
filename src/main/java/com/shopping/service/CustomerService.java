package com.shopping.service;

import com.shopping.domain.dto.RegistrationRequest;
import com.shopping.domain.dto.CustomerResponse;
import com.shopping.domain.model.Customer;
import com.shopping.domain.model.User;

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
