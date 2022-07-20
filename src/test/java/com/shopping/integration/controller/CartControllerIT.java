package com.shopping.integration.controller;

import com.shopping.AbstractIT;
import com.shopping.IT;
import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.CartItemRequest;
import com.shopping.domain.dto.CartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@IT
@Sql(scripts = "classpath:sql/cart.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartControllerIT extends AbstractIT {
    
    @BeforeEach
    void setUp() {
        headers.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + generateUserToken());
    }

    @Test
    public void it_should_add_item_to_cart() {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .serialNumber("PADMA232")
                .quantity(2)
                .build();

        // when
        ResponseEntity<ApiResponse<CartResponse>> response = restTemplate
                .exchange("/api/v1/cart",
                        HttpMethod.POST,
                        new HttpEntity<>(cartItemRequest, headers),
                        new ParameterizedTypeReference<ApiResponse<CartResponse>>() {
                        });

        CartResponse cartResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(cartResponse, "Returned must not be null");
        assertTrue(cartResponse.getTotalQuantity() >= 2, "Total quantity must be equal");
        assertTrue(cartResponse.getItems().stream().anyMatch(item -> item.getSerialNumber().equals("PADMA232")));
        assertEquals(cartResponse.getItems().stream().filter(item -> item.getSerialNumber().equals("PADMA232")).findFirst().get(),
                cartResponse.getItems().stream().filter(item -> item.getSerialNumber().equals("PADMA232")).findFirst().get());

    }

    @Test
    public void it_should_return_cart_of_that_customer() {
        // when
        ResponseEntity<ApiResponse<CartResponse>> response = restTemplate
                .exchange("/api/v1/cart",
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        new ParameterizedTypeReference<ApiResponse<CartResponse>>() {
                        });

        CartResponse cartResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(cartResponse, "Returned must not be null");
        assertNotEquals(0, cartResponse.getItems().size(), "Items size must be equal");
        assertTrue(cartResponse.getItems().stream().anyMatch(item -> item.getSerialNumber().equals("KMNA239")));

    }

    @Test
    public void it_should_delete_item_from_cart() {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .serialNumber("KMNA239")
                .build();

        // when
        ResponseEntity<ApiResponse<CartResponse>> response = restTemplate
                .exchange("/api/v1/cart/KMNA239",
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, headers),
                        new ParameterizedTypeReference<ApiResponse<CartResponse>>() {
                        });

        CartResponse cartResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(cartResponse, "Returned must not be null");
        assertFalse(cartResponse.getItems().stream().anyMatch(item -> item.getSerialNumber().equals("KMNA239")));
    }

    @Test
    public void it_should_delete_all_items_from_cart() {
        // when
        ResponseEntity<ApiResponse<CartResponse>> response = restTemplate
                .exchange("/api/v1/cart/empty",
                        HttpMethod.DELETE,
                        new HttpEntity<>(null, headers),
                        new ParameterizedTypeReference<ApiResponse<CartResponse>>() {
                        });

        CartResponse cartResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(cartResponse, "Returned must not be null");
        assertEquals(0, cartResponse.getItems().size(), "Items size must be zero");
        assertFalse(cartResponse.getItems().stream().anyMatch(item -> item.getSerialNumber().equals("KMNA239")));
    }

    @Test
    public void it_should_update_item_from_cart() {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .serialNumber("KMNA239")
                .quantity(5)
                .build();

        // when
        ResponseEntity<ApiResponse<CartResponse>> response = restTemplate
                .exchange("/api/v1/cart",
                        HttpMethod.PUT,
                        new HttpEntity<>(cartItemRequest, headers),
                        new ParameterizedTypeReference<ApiResponse<CartResponse>>() {
                        });

        CartResponse cartResponse = response.getBody().getData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code must be equal");
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertNotNull(cartResponse, "Returned must not be null");
        assertTrue(cartResponse.getItems().stream().anyMatch(item -> item.getSerialNumber().equals("KMNA239")
                && item.getQuantity().equals(5)));

    }
}
