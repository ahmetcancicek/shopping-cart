package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.config.JwtTokenUtil;
import com.shopping.dto.AuthRequest;
import com.shopping.dto.RegistrationRequest;
import com.shopping.dto.CustomerResponse;
import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.model.User;
import com.shopping.repository.CustomerRepository;
import com.shopping.service.CustomerService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CustomerService customerService;

    @MockBean(name = "userService")
    private UserDetailsService userService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;


    @Test
    public void it_should_register_customer() throws Exception {
        // given
        RegistrationRequest customerRequest = RegistrationRequest.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .password("ADl362AMA")
                .build();


        CustomerResponse customerResponse = CustomerResponse.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .build();

        UserDetails user = User.builder()
                .id(1L)
                .username("bruceking")
                .password(passwordEncoder.encode("ADl362AMA"))
                .active(true)
                .email("bruce@email.com")
                .build();

        given(userService.loadUserByUsername(any())).willReturn(user);
        given(customerService.save(any())).willReturn(customerResponse);


        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.username").value("bruceking"))
                .andDo(print());
    }

    @Test
    public void it_should_login_customer() throws Exception {
        // given
        AuthRequest authRequest = AuthRequest.builder()
                .username("bruceking")
                .password("ADl362AMA")
                .build();

        CustomerResponse customerResponse = CustomerResponse.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .build();

        UserDetails user = User.builder()
                .id(1L)
                .username("bruceking")
                .password(passwordEncoder.encode("ADl362AMA"))
                .active(true)
                .email("bruce@email.com")
                .build();

        given(userService.loadUserByUsername(any())).willReturn(user);


        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.username").value("bruceking"))
                .andDo(print());
    }


    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing() throws Exception {
        // given
        RegistrationRequest customerRequest = RegistrationRequest.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .password("ADl362AMA")
                .build();

        CustomerResponse customerResponse = CustomerResponse.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .build();

        given(customerService.save(any())).willThrow(new AlreadyExistsElementException("user already exist"));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerRequest));

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void it_should_return_client_error_when_register_customer_with_body_isNotValid() throws Exception {
        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(RegistrationRequest.builder().build()));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError());
    }

}