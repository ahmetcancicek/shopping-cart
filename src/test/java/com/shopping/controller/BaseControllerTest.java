package com.shopping.controller;

import com.shopping.config.JwtTokenUtil;
import com.shopping.repository.CustomerRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BaseControllerTest {
    @MockBean(name = "userService")
    public UserDetailsService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;
}
