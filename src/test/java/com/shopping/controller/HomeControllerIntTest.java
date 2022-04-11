package com.shopping.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void it_should_return_message() {
        ResponseEntity<String> getString = restTemplate.exchange("/hello",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                String.class);

        assertEquals("Hello, World", getString.getBody());
    }

    @Test
    public void it_should_return_message_when_given_parameter() {
        ResponseEntity<String> getString = restTemplate.exchange("/hello?name=John",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                String.class);

        assertEquals("Hello, John", getString.getBody());
    }
}