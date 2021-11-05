package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.servivce.impl.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CustomerService customerService;

    @Test
    void registerCustomer_success() throws Exception {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .users(new User("email@email.com", "username", "password", true))
                .build();

        Mockito.when(customerService.saveCustomer(customer)).thenReturn(customer);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("First Name")));
    }
}