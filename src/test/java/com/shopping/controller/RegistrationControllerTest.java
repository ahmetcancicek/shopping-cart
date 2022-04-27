package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.exception.UserAlreadyExistsException;
import com.shopping.exception.UserNotFoundException;
import com.shopping.model.Customer;
import com.shopping.model.User;
import com.shopping.service.CustomerService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CustomerService customerService;

    @Test
    public void it_should_register_customer() throws Exception {
        // given
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

        when(customerService.save(customer)).thenReturn(customer);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("First Name"));
    }

    @Test
    public void it_should_delete_customer() throws Exception {
        // given
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

        when(customerService.findById(1L)).thenReturn(customer);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/registration/{id}", "1");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

    }

    @Test
    public void it_should_return_bad_request_when_delete_customer_with_does_not_exist() throws Exception {
        // given
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

        doThrow(new UserNotFoundException("user does not exist")).when(customerService).deleteById(any());

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/registration/{id}", "1");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing() throws Exception {
        // given
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

        when(customerService.save(any())).thenThrow(new UserAlreadyExistsException("user already exist"));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void it_should_return_client_error_when_register_customer_with_body_isNotValid() throws Exception {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .user(new User())
                .build();


        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError());
    }

}