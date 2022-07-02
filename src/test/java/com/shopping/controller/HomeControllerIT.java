package com.shopping.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerIT extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + getToken());
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