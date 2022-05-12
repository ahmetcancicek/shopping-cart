package com.shopping.service.impl;

import com.shopping.dto.RegistrationRequest;
import com.shopping.dto.CustomerResponse;
import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.CustomerMapper;
import com.shopping.model.Cart;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.repository.CustomerRepository;
import com.shopping.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<CustomerResponse> findAll() {
        return CustomerMapper.INSTANCE.fromCustomers(customerRepository.findAll());
    }

    @Transactional
    @Override
    public CustomerResponse save(RegistrationRequest customerPayload) {
        isExisted(customerPayload);

        // Cart initialize
        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerPayload);
        customer.setCart(Cart.builder().customer(customer).totalPrice(BigDecimal.ZERO).build());

        // Password encode
        customer.getUser().setPassword(passwordEncoder.encode(customer.getUser().getPassword()));
        customer.getUser().setActive(true);

        Customer savedCustomer = customerRepository.save(customer);
        log.info("new customer has been created with username: {}", savedCustomer.getUser().getUsername());

        return CustomerMapper.INSTANCE.fromCustomer(savedCustomer);
    }

    private void isExisted(RegistrationRequest customerPayload) {
        customerRepository.findByUser_Username(customerPayload.getUsername()).ifPresent((it) -> {
            log.error("customer already exists with username: {}", customerPayload.getUsername());
            throw new AlreadyExistsElementException("customer already exists with username: {" + customerPayload.getUsername() + "}");
        });

        customerRepository.findByUser_Email(customerPayload.getEmail()).ifPresent((it) -> {
            log.error("customer already exists with email: {}", customerPayload.getEmail());
            throw new AlreadyExistsElementException("customer already exists with email: {" + customerPayload.getEmail() + "}");
        });
    }

    @Override
    public CustomerResponse findById(Long id) {
        return CustomerMapper.INSTANCE.fromCustomer(customerRepository.findById(id).orElseThrow(() -> {
            log.error("customer does not exist with id: {}", id);
            return new NoSuchElementFoundException(String.format("customer does not exist with id: {%d}", id));
        }));
    }

    @Override
    public CustomerResponse findByUser(User user) {
        return CustomerMapper.INSTANCE.fromCustomer(customerRepository.findByUser(user).orElseThrow(() -> {
            log.error("customer does not exist");
            return new NoSuchElementFoundException(String.format("customer does not exist"));
        }));
    }

    @Override
    public CustomerResponse findByUsername(String username) {
        return CustomerMapper.INSTANCE.fromCustomer(customerRepository.findByUser_Username(username).orElseThrow(() -> {
            log.error("customer does not exist with username: {}", username);
            return new NoSuchElementFoundException(String.format("customer does not exist with username: %s", username));
        }));
    }

    @Override
    public CustomerResponse findByEmail(String email) {
        return CustomerMapper.INSTANCE.fromCustomer(customerRepository.findByUser_Email(email).orElseThrow(() -> {
            log.error("customer does not exist with email: {}", email);
            return new NoSuchElementFoundException(String.format("customer does not exist with email: %s", email));
        }));
    }

    @Override
    public Customer findCustomerByUsername(String username) {
        return customerRepository.findByUser_Username(username).orElseThrow(() -> {
            log.error("customer does not exist with username: {}", username);
            return new NoSuchElementFoundException(String.format("customer does not exist with username: %s", username));
        });
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        CustomerResponse customerResponse = findById(id);
        customerRepository.deleteById(id);
        log.info("customer has been deleted: {}", customerResponse.toString());
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        CustomerResponse customerResponse = findByUsername(username);
        customerRepository.deleteByUser_Username(username);
        log.info("customer has been deleted: {}", customerResponse.toString());
    }
}
