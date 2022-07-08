package com.shopping.controller.unit;

import com.shopping.config.jwt.JwtTokenUtil;
import com.shopping.repository.CustomerRepository;
import com.shopping.service.RoleService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BaseControllerTest {
    @MockBean(name = "userService")
    public UserDetailsService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @MockBean
    public RoleService roleService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;
}
