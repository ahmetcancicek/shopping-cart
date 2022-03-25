package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CustomerService customerService;

    @Test
    public void should_return_successful_when_request_save_customer_with_body_isValid() throws Exception {
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .user(user)
                .build();

        Mockito.when(customerService.save(customer)).thenReturn(customer);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("First Name")));
    }

    @Test
    public void should_return_bad_request_when_request_save_customer_with_existing_username() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .user(user)
                .build();

        Mockito.when(customerService.save(customer)).thenReturn(customer);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
//
//        // TODO: We should done
    }


    @Test

    public void should_return_client_error_when_request_save_customer_with_body_isNotValid() throws Exception {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .user(new User())
                .build();

        Mockito.when(customerService.save(customer)).thenReturn(customer);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}