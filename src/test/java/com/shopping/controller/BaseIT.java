package com.shopping.controller;

import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.AuthRequest;
import com.shopping.domain.dto.AuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(value = {"/import.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseIT {

    @LocalServerPort
    private int port;
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql");
    @Autowired
    private TestRestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();
    private static boolean init = false;

    private static String token = null;

    public static String getToken() {
        return token;
    }

    @BeforeEach
    void setUp() {
        if (!init) {
            AuthRequest request = AuthRequest.builder()
                    .username("lucycar")
                    .password("DHN827D9N")
                    .build();

            // when
            ResponseEntity<ApiResponse<AuthToken>> response = restTemplate.exchange("/api/auth/login",
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    new ParameterizedTypeReference<ApiResponse<AuthToken>>() {
                    });

            AuthToken user = Objects.requireNonNull(response.getBody()).getData();
            token = user.getToken();
        }
    }

    @Test
    public void it_should_db_run() {
        assertTrue(mysql.isRunning(), "MySQL is not running");
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
