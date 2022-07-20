package com.shopping.integration.controller;

import com.shopping.AbstractIT;
import com.shopping.IT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@IT
@Sql(value = {"/sql/auth.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class HomeControllerIT extends AbstractIT {

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setUp() {
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + generateUserToken());
    }

    @Test
    public void it_should_return_message() {
        ResponseEntity<String> getString = restTemplate.exchange("/api/v1/hello",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                String.class);

        assertEquals("Hello, World", getString.getBody());
    }

    @Test
    public void it_should_return_message_when_given_parameter() {
        ResponseEntity<String> getString = restTemplate.exchange("/api/v1/hello?name=John",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                String.class);

        assertEquals("Hello, John", getString.getBody());
    }
}