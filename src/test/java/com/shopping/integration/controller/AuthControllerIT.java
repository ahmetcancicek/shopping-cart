package com.shopping.integration.controller;

import com.shopping.domain.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerIT extends AbstractIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void it_should_register_customer() {
        // given
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("Bruce")
                .lastName("King")
                .email("bruce@email.com")
                .username("bruceking")
                .password("ADl362AMA")
                .build();

        // when
        ResponseEntity<ApiResponse<AuthToken>> response = restTemplate.exchange("/api/v1/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<AuthToken>>() {
                });

        AuthToken user = Objects.requireNonNull(response.getBody()).getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus(), "Status code must be equal");
        assertEquals(request.getUsername(), user.getUsername(), "Username must be equal");
        assertNotNull(user.getToken(), "Token must not be null");

    }

    @Test
    public void it_should_login_customer() {
        // given
        AuthRequest request = AuthRequest.builder()
                .username("lucycar")
                .password("DHN827D9N")
                .build();

        // when
        ResponseEntity<ApiResponse<AuthToken>> response = restTemplate.exchange("/api/v1/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<AuthToken>>() {
                });

        AuthToken user = Objects.requireNonNull(response.getBody()).getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus(), "Status code must be equal");
        assertEquals(request.getUsername(), user.getUsername(), "Username must be equal");
        assertNotNull(user.getToken(), "Token must not be null");
    }

    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing_email() {
        // given
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("Lucy")
                .lastName("Car")
                .email("lucycar@email.com")
                .username("lucycar1")
                .password("ADl362AMA")
                .build();

        // when
        ResponseEntity<ApiResponse<CustomerResponse>> response = restTemplate.exchange("/api/v1/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<CustomerResponse>>() {
                });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing_username() {
        // given
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("Lucy")
                .lastName("Car")
                .email("lucycar1@email.com")
                .username("lucycar")
                .password("ADl362AMA")
                .build();

        // when
        ResponseEntity<ApiResponse<CustomerResponse>> response = restTemplate.exchange("/api/v1/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<CustomerResponse>>() {
                });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus(), "Status code must be equal");
    }

    @Test
    public void it_should_return_client_error_when_register_customer_with_body_isNotValid() {
        // given
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("Bill")
                .lastName("King")
                .password("ADl362AMA")
                .build();

        // when
        ResponseEntity<ApiResponse<CustomerResponse>> response = restTemplate.exchange("/api/v1/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<CustomerResponse>>() {
                });


        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }
}