package com.shopping.controller;

import com.shopping.domain.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductControllerIT extends BaseIT {
    @Autowired
    private TestRestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();

    @Test
    public void it_should_add_product() {
        // when
        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + getAccessToken());

        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/products",
                        HttpMethod.POST,
                        new HttpEntity<>(productRequest, headers),
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        });

        ProductResponse productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code must be equal");
        assertEquals("success", response.getBody().getMessage(), "It must be success");
        assertEquals(productRequest.quantity, productResponse.getQuantity(), "Quantity must be equal");
    }

    @Test
    public void it_should_delete_product_of_that_serialNumber() {
        // when
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + getAccessToken());

        ResponseEntity<Void> response = restTemplate.exchange("/api/products/{serialNumber}",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                Void.class,
                "PADMA232");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_serialNumber_does_not_exist() {
        // when
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + getAccessToken());

        ResponseEntity<ApiResponse<String>> response = restTemplate.exchange("/api/products/{serialNumber}",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<ApiResponse<String>>() {
                },
                "MADJ349");

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_product_of_that_serialNumber() {
        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/products/{serialNumber}",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        },
                        "PADMA232");

        ProductResponse productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals("success", response.getBody().getMessage(), "It must be success");
        assertEquals("Galaxy S22", productResponse.getName(), "Name must be equal");
    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() {
        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/products/{serialNumber}",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        },
                        "A32TL2B8");

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code must be equal");
    }

    @Test
    public void it_should_return_list_of_all_products() {
        // when
        ResponseEntity<ApiResponse<List<ProductResponse>>> response = restTemplate
                .exchange("/api/products",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<ApiResponse<List<ProductResponse>>>() {
                        });

        List<ProductResponse> productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(productResponse, "List must not be null");
        assertNotEquals(0, productResponse.size(), "List must have at least 1 element");


    }

    @Test
    public void it_should_update_product_of_that_serialNumber() {
        // given
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + getAccessToken());

        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("KMNA239")
                .name("iPhone 13 PRO")
                .description("Apple iPhone 13 PRO")
                .price(BigDecimal.valueOf(900))
                .quantity(3)
                .build();

        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/products",
                        HttpMethod.PUT,
                        new HttpEntity<>(productRequest, headers),
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        });

        ProductResponse productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertNotNull(productRequest, "Returned must not be null");
        assertEquals(productRequest.quantity, productResponse.getQuantity(), "Quantity must be equal");
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
