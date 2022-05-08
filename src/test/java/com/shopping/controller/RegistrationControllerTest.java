package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.dto.CustomerRequest;
import com.shopping.dto.CustomerResponse;
import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
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
import static org.mockito.BDDMockito.given;
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
        CustomerRequest customerRequest = CustomerRequest.builder()
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

        given(customerService.save(any())).willReturn(customerResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("bruceking"))
                .andExpect(jsonPath("$.firstName").value("Bruce"));
    }

    @Test
    public void it_should_delete_customer() throws Exception {
        // given
        CustomerResponse customerResponse = CustomerResponse.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .build();

        given(customerService.findByUsername(any())).willReturn(customerResponse);

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
        doThrow(new NoSuchElementFoundException("user does not exist")).when(customerService).deleteByUsername(any());

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/registration/{username}", "bruceking");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing() throws Exception {
        // given
        CustomerRequest customerRequest = CustomerRequest.builder()
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
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerRequest));

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void it_should_return_client_error_when_register_customer_with_body_isNotValid() throws Exception {
        // given
        CustomerRequest customerRequest = CustomerRequest.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .password("ADl362AMA")
                .build();

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError());
    }

}