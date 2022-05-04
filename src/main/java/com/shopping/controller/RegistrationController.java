package com.shopping.controller;

import com.shopping.dto.CustomerRequest;
import com.shopping.dto.CustomerResponse;
import com.shopping.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class RegistrationController {

    private final CustomerService customerService;

    @Autowired
    public RegistrationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public CustomerResponse registerCustomer(@Valid @RequestBody CustomerRequest customer) {
        return customerService.save(customer);
    }

    @DeleteMapping("/registration/{username}")
    public void deleteCustomer(@PathVariable("username") String username) {
        customerService.deleteByUsername(username);
    }
}
