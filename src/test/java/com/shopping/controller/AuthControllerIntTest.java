package com.shopping.controller;

import com.shopping.dto.*;
import com.shopping.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(value = {"/import.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/clear.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthControllerIntTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

    private final HttpHeaders headers = new HttpHeaders();

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
        ResponseEntity<ApiResponse<AuthToken>> response = restTemplate.exchange("/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<AuthToken>>() {
                });

        AuthToken user = Objects.requireNonNull(response.getBody()).getResult();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals("success", response.getBody().getMessage(),"It must be success");
        assertEquals(request.getUsername(), user.getUsername(),"Username must be equal");
        assertNotNull(user.getToken(),"Token must not be null");

    }

    @Test
    public void it_should_login_customer() {
        // given
        AuthRequest request = AuthRequest.builder()
                .username("bruceking")
                .password("ADl362AMA")
                .build();

        // when
        ResponseEntity<ApiResponse<AuthToken>> response = restTemplate.exchange("/api/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<AuthToken>>() {
                });

        AuthToken user = Objects.requireNonNull(response.getBody()).getResult();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals("success", response.getBody().getMessage(),"It must be success");
        assertEquals(request.getUsername(), user.getUsername(),"Username must be equal");
        assertNotNull(user.getToken(),"Token must not be null");


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
        ResponseEntity<ApiResponse<CustomerResponse>> response = restTemplate.exchange("/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<CustomerResponse>>() {
                });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
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
        ResponseEntity<ApiResponse<CustomerResponse>> response = restTemplate.exchange("/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<CustomerResponse>>() {
                });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
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
        ResponseEntity<ApiResponse<CustomerResponse>> response = restTemplate.exchange("/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<ApiResponse<CustomerResponse>>() {
                });


        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> " always");
        registry.add("spring.jpa.defer-datasource-initialization", () -> "true");
        registry.add("spring.jpa.hibernate.dll-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}