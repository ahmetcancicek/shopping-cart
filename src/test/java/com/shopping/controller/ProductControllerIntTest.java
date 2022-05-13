package com.shopping.controller;

import com.shopping.domain.dto.ProductRequest;
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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(value = {"/import.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/clear.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql");

    private final HttpHeaders headers = new HttpHeaders();

    @Test
    public void it_should_add_product() {
        // given
        ProductRequest productPayload = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        // when
        ResponseEntity<ProductRequest> response = restTemplate.exchange("/products",
                HttpMethod.POST,
                new HttpEntity<>(productPayload, headers),
                ProductRequest.class);

        ProductRequest createdProductPayload = response.getBody();

        // then
        assertNotNull(createdProductPayload, "Returned must not be null");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code must be equal");
        assertEquals(productPayload.quantity, createdProductPayload.getQuantity(), "Quantity must be equal");
    }

    @Test
    public void it_should_delete_product_of_that_serialNumber() {
        // when
        ResponseEntity<Void> response = restTemplate.exchange("/products/{serialNumber}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                "PADMA232");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_serialNumber_does_not_exist() {
        // when
        ResponseEntity<Void> response = restTemplate.exchange("/products/{serialNumber}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                "MADJ349");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_product_of_that_serialNumber() {
        // when
        ResponseEntity<ProductRequest> response = restTemplate.exchange("/products/{serialNumber}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                ProductRequest.class,
                "PADMA232");

        ProductRequest expectedProductPayload = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(expectedProductPayload, "Returned must not be null");
        assertEquals("Galaxy S22", expectedProductPayload.getName(), "Name must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() {
        // when
        ResponseEntity<ProductRequest> response = restTemplate.exchange("/products/{serialNumber}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                ProductRequest.class,
                "024290420");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_list_of_all_products() {
        // when
        ResponseEntity<List<ProductRequest>> response = restTemplate.exchange("/products",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ProductRequest>>() {
                });

        List<ProductRequest> productPayloads = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(productPayloads, "List must not be null");
        assertNotEquals(0, productPayloads.size(), "List must have at least 1 element");


    }

    @Test
    public void it_should_update_product_of_that_serialNumber() {
        // given
        ProductRequest productPayload = ProductRequest.builder()
                .serialNumber("KMNA239")
                .name("iPhone 13 PRO")
                .description("Apple iPhone 13 PRO")
                .price(BigDecimal.valueOf(900))
                .quantity(3)
                .build();

        // when
        ResponseEntity<ProductRequest> response = restTemplate.exchange("/products",
                HttpMethod.PUT,
                new HttpEntity<>(productPayload, headers),
                ProductRequest.class);

        ProductRequest expectedProductPayload = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(expectedProductPayload, "Returned must not be null");
        assertEquals(productPayload.quantity, expectedProductPayload.getQuantity(), "Quantity must be equal");
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
