package com.shopping.controller;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(value = {"/import.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/clear.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RegistrationControllerIntTest {

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
        User user = User.builder()
                .username("lucycar")
                .password("37m38an")
                .email("lucycar12@email.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("Lucy")
                .lastName("Car")
                .user(user)
                .build();

        // when
        ResponseEntity<Customer> response = restTemplate.exchange("/registration",
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                Customer.class);

        Customer createdCustomer = response.getBody();

        // then
        assertNotNull(createdCustomer,"Returned must be equal");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code must be equal");
        assertEquals("Lucy Car", String.format("%s %s", createdCustomer.getFirstName(), createdCustomer.getLastName()));
        ;
    }

    @Test
    public void it_should_delete_customer() {
        // when
        ResponseEntity<Void> response = restTemplate.exchange("/registration/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                "1000");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_delete_customer_with_does_not_exist() {
        // when
        ResponseEntity<Void> response = restTemplate.exchange("/registration/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                "1232000");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing_email() {
        // given
        User user = User.builder()
                .id(2000L)
                .username("john-doe")
                .password("j7aad3")
                .email("johndoe@mock.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .id(2000L)
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        // when
        ResponseEntity<Customer> response = restTemplate.exchange("/registration",
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                Customer.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_register_customer_with_existing_username() {
        // given
        User user = User.builder()
                .username("johndoe")
                .password("j7aad3")
                .email("john.doe@mock.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .user(user)
                .build();

        // when
        ResponseEntity<Customer> response = restTemplate.exchange("/registration",
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                Customer.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_client_error_when_register_customer_with_body_isNotValid() {
        // given
        User user = User.builder()
                .username("selenahouse")
                .password("37m38an")
                .email("selenahouse@email.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("Selena")
                .lastName("House")
                .user(null)
                .build();

        // when
        ResponseEntity<Customer> response = restTemplate.exchange("/registration",
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                Customer.class);

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