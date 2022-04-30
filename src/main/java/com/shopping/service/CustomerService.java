package com.shopping.service;

import com.shopping.dto.CustomerPayload;
import com.shopping.model.User;

import java.util.List;

public interface CustomerService {
    List<CustomerPayload> findAll();
    CustomerPayload save(CustomerPayload customer);
    CustomerPayload findById(Long id);
    CustomerPayload findByUser(User user);

    CustomerPayload findByUsername(String username);

    CustomerPayload findByEmail(String email);
    void deleteById(Long id);

    void deleteByUsername(String username);
}
