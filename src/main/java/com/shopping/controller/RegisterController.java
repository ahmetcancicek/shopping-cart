package com.shopping.controller;

import com.shopping.model.Customer;
import com.shopping.servivce.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class RegisterController {

    private final CustomerService customerService;

    @Autowired
    public RegisterController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customer/register", method = RequestMethod.POST)
    public Customer registerCustomer(@Valid @RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }
}
