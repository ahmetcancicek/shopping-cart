package com.shopping.service.impl;

import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.repository.CartRepository;
import com.shopping.repository.CustomerRepository;
import com.shopping.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        // TODO: Fix cart
        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        log.info("new customer has been created: {}", savedCustomer.getFirstName());
        return savedCustomer;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
}
