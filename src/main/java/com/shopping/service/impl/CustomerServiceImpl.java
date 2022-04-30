package com.shopping.service.impl;

import com.shopping.dto.CustomerPayload;
import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.CustomerMapper;
import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.repository.CustomerRepository;
import com.shopping.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerPayload> findAll() {
        return CustomerMapper.INSTANCE.toCustomerPayloads(customerRepository.findAll());
    }

    @Transactional
    @Override
    public CustomerPayload save(CustomerPayload customerPayload) {
        customerRepository.findByUser_Username(customerPayload.getUsername()).ifPresent((it) -> {
            log.error("customer already exists with username: {}", customerPayload.getUsername());
            throw new AlreadyExistsElementException("customer already exists with username: {" + customerPayload.getUsername() + "}");
        });

        customerRepository.findByUser_Email(customerPayload.getEmail()).ifPresent((it) -> {
            log.error("customer already exists with email: {}", customerPayload.getEmail());
            throw new AlreadyExistsElementException("customer already exists with email: {" + customerPayload.getEmail() + "}");
        });

        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerPayload);
        customer.setCart(Cart.builder().customer(customer).totalPrice(BigDecimal.ZERO).build());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("new customer has been created with username: {}", savedCustomer.getUser().getUsername());
        return CustomerMapper.INSTANCE.toCustomerPayload(savedCustomer);
    }

    @Override
    public CustomerPayload findById(Long id) {
        return CustomerMapper.INSTANCE.toCustomerPayload(customerRepository.findById(id).orElseThrow(() -> {
            log.error("customer does not exist with id: {}", id);
            return new NoSuchElementFoundException(String.format("customer does not exist with id: {%d}", id));
        }));
    }

    @Override
    public CustomerPayload findByUser(User user) {
        return CustomerMapper.INSTANCE.toCustomerPayload(customerRepository.findByUser(user).orElseThrow(() -> {
            log.error("customer does not exist");
            return new NoSuchElementFoundException(String.format("customer does not exist"));
        }));
    }

    @Override
    public CustomerPayload findByUsername(String username) {
        return CustomerMapper.INSTANCE.toCustomerPayload(customerRepository.findByUser_Username(username).orElseThrow(() -> {
            log.error("customer does not exist with username: {}", username);
            return new NoSuchElementFoundException(String.format("customer does not exist with username: {%s}", username));
        }));
    }

    @Override
    public CustomerPayload findByEmail(String email) {
        return CustomerMapper.INSTANCE.toCustomerPayload(customerRepository.findByUser_Email(email).orElseThrow(() -> {
            log.error("customer does not exist with email: {}", email);
            return new NoSuchElementFoundException(String.format("customer does not exist with email: {%s}", email));
        }));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        CustomerPayload customerPayload = findById(id);
        customerRepository.deleteById(id);
        log.info("customer has been deleted: {}", customerPayload.toString());
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        CustomerPayload customerPayload = findByUsername(username);
        customerRepository.deleteByUser_Username(username);
        log.info("customer has been deleted: {}", customerPayload.toString());
    }
}
