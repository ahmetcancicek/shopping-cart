package com.shopping.controller;

import com.shopping.exception.UserNotFoundException;
import com.shopping.model.Customer;
import com.shopping.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
public class RegistrationController {

    private final CustomerService customerService;

    @Autowired
    public RegistrationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public Customer registerCustomer(@Valid @RequestBody Customer customer) {
        // TODO: Check If it is null
        return customerService.save(customer);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/registration/{customerId}")
    public void deleteCustomer(@PathVariable String customerId) {
        Optional<Customer> customerOptional = customerService.findById(Long.valueOf(customerId));
        if (customerOptional.isEmpty())
            throw new UserNotFoundException("Customer does not exist");

        customerService.deleteById(Long.valueOf(customerId));
    }
}
