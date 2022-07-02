package com.shopping.controller;

import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.ProductRequest;
import com.shopping.domain.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductControllerIT extends AbstractIT {
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
    public void it_should_add_product() {
        // given
        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("Y5N3DJ")
                .name("Egg")
                .description("Super egg")
                .price(BigDecimal.valueOf(10.0))
                .quantity(10)
                .build();

        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/v1/products",
                        HttpMethod.POST,
                        new HttpEntity<>(productRequest, headers),
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        });

        ProductResponse productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
        assertNotNull(productResponse, "Returned must not be null");
        assertEquals(productRequest.getSerialNumber(), productResponse.getSerialNumber(), "Serial number must be equal");
        assertEquals(productRequest.getName(), productResponse.getName(), "Name must be equal");
        assertEquals(productRequest.getDescription(), productResponse.getDescription(), "Description must be equal");
        assertEquals(productRequest.getPrice(), productResponse.getPrice(), "Price must be equal");
        assertEquals(productRequest.quantity, productResponse.getQuantity(), "Quantity must be equal");
    }

    @Test
    public void it_should_delete_product_of_that_serialNumber() {
        // when
        ResponseEntity<ApiResponse<String>> response = restTemplate.exchange("/api/v1/products/{serialNumber}",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<ApiResponse<String>>() {
                },
                "PADMA232");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());

    }

    @Test
    public void it_should_return_bad_request_when_delete_product_of_that_serialNumber_does_not_exist() {
        // when
        ResponseEntity<ApiResponse<String>> response = restTemplate.exchange("/api/products/{serialNumber}",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<ApiResponse<String>>() {
                },
                "MADJ349");

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());

    }

    @Test
    public void it_should_return_product_of_that_serialNumber() {
        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/v1/products/{serialNumber}",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        },
                        "PADMA232");

        ProductResponse productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must not be null");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(productResponse, "Returned must not be null");
        assertNotNull(productResponse.getSerialNumber(), "Serial number must not be null");
        assertNotNull(productResponse.getName(), "Name must not be null");
        assertNotNull(productResponse.getPrice(), "Price must not be null");
        assertNotNull(productResponse.getQuantity(), "Quantity must not be null");

    }

    @Test
    public void it_should_return_bad_request_when_product_does_not_exist() {
        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/v1/products/{serialNumber}",
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
                .exchange("/api/v1/products",
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<ApiResponse<List<ProductResponse>>>() {
                        });

        List<ProductResponse> productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(productResponse, "List must not be null");
        assertNotEquals(0, productResponse.size(), "List must have at least 1 element");
        assertNotNull(productResponse.get(0).getSerialNumber(), "Serial number must not be null");
        assertNotNull(productResponse.get(0).getName(), "Name must not be null");
        assertNotNull(productResponse.get(0).getPrice(), "Price must not be null");
        assertNotNull(productResponse.get(0).getQuantity(), "Quantity must not be null");


    }

    @Test
    public void it_should_update_product_of_that_serialNumber() {
        // given
        ProductRequest productRequest = ProductRequest.builder()
                .serialNumber("KMNA239")
                .name("iPhone 13 PRO")
                .description("Apple iPhone 13 PRO")
                .price(BigDecimal.valueOf(900))
                .quantity(3)
                .build();

        // when
        ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate
                .exchange("/api/v1/products",
                        HttpMethod.PUT,
                        new HttpEntity<>(productRequest, headers),
                        new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {
                        });

        ProductResponse productResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(productResponse, "Returned must not be null");
        assertEquals(productRequest.getSerialNumber(), productResponse.getSerialNumber(), "Serial number must be equal");
        assertEquals(productRequest.getName(), productResponse.getName(), "Name must be equal");
        assertEquals(productRequest.getDescription(), productResponse.getDescription(), "Description must be equal");
        assertEquals(productRequest.getPrice(), productResponse.getPrice(), "Price must be equal");
        assertEquals(productRequest.quantity, productResponse.getQuantity(), "Quantity must be equal");
    }
}
