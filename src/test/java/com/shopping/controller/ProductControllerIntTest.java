package com.shopping.controller;

import com.shopping.model.Product;
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
        Product product = Product.builder()
                .name("Product 1")
                .quantity(5)
                .price(BigDecimal.valueOf(12.50))
                .build();

        // when
        ResponseEntity<Product> response = restTemplate.exchange("/products",
                HttpMethod.POST,
                new HttpEntity<>(product, headers),
                Product.class);

        Product createdProduct = response.getBody();

        // then
        assertNotNull(createdProduct, "Returned must not be null");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code must be equal");
        assertEquals(5, createdProduct.getQuantity(), "Quantity must be equal");
    }

    @Test
    public void it_should_delete_product_of_that_id() {
        // when
        ResponseEntity<Void> response = restTemplate.exchange("/products/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                "3000");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_id_does_not_exist() {
        // when
        ResponseEntity<Void> response = restTemplate.exchange("/products/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                "39139839");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_product_of_that_id() {
        // when
        ResponseEntity<Product> response = restTemplate.exchange("/products/{id}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Product.class,
                "3001");

        Product expectedProduct = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(expectedProduct, "Returned must not be null");
        assertEquals("Galaxy S22", expectedProduct.getName(), "Name must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() {
        // when
        ResponseEntity<Product> response = restTemplate.exchange("/products/{id}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Product.class,
                "024290420");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_list_of_all_products() {
        // when
        ResponseEntity<List<Product>> response = restTemplate.exchange("/products",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Product>>() {
                });

        List<Product> products = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(products, "List must not be null");
        assertNotEquals(0, products.size(), "List must have at least 1 element");


    }

    @Test
    public void it_should_update_product_of_that_id() {
        // given
        Product product = Product.builder()
                .id(3001L)
                .description("Apple iPhone 13 PRO")
                .name("iPhone 13 PRO")
                .quantity(50)
                .price(BigDecimal.valueOf(1200))
                .build();

        // when
        ResponseEntity<Product> response = restTemplate.exchange("/products",
                HttpMethod.PUT,
                new HttpEntity<>(product, headers),
                Product.class);

        Product expectedProduct = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(expectedProduct, "Returned must not be null");
        assertEquals(50, expectedProduct.getQuantity(), "Quantity must be equal");
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
