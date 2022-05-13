package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.domain.dto.CartItemRequest;
import com.shopping.domain.dto.CartItemResponse;
import com.shopping.domain.dto.CartResponse;
import com.shopping.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void it_should_add_item_to_cart() throws Exception {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .username("billking")
                .serialNumber("Y5N3DJ")
                .quantity(1)
                .build();

        CartResponse cartResponse = CartResponse.builder()
                .username("billking")
                .totalQuantity(1)
                .totalPrice(BigDecimal.TEN)
                .items(new HashSet<>(Set.of(CartItemResponse.builder()
                        .price(BigDecimal.TEN)
                        .name("Egg")
                        .description("Super egg")
                        .quantity(1)
                        .serialNumber("Y5N3DJ")
                        .build())))
                .build();

        given(cartService.addItemToCart(cartItemRequest)).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartItemRequest));

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.totalQuantity").value(1))
                .andExpect(jsonPath("$.username").value(cartResponse.getUsername()));
    }

    @Test
    public void it_should_return_cart_of_that_customer() throws Exception {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .username("billking")
                .serialNumber("Y5N3DJ")
                .quantity(1)
                .build();

        CartResponse cartResponse = CartResponse.builder()
                .username("billking")
                .totalQuantity(1)
                .totalPrice(BigDecimal.TEN)
                .items(new HashSet<>(Set.of(CartItemResponse.builder()
                        .price(BigDecimal.TEN)
                        .name("Egg")
                        .description("Super egg")
                        .quantity(1)
                        .serialNumber("Y5N3DJ")
                        .build())))
                .build();

        given(cartService.findByUsername(any())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/carts/{username}", cartResponse.getUsername())
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(cartResponse.getUsername()));
    }

    @Test
    public void it_should_delete_item_from_cart() throws Exception {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .username("billking")
                .serialNumber("Y5N3DJ")
                .quantity(1)
                .build();

        CartResponse cartResponse = CartResponse.builder()
                .username("billking")
                .totalQuantity(0)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>())
                .build();

        given(cartService.deleteItemFromCart(cartItemRequest.getUsername(), cartItemRequest.getSerialNumber())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/carts/{username}?serialNumber={serialNumber}", cartItemRequest.getUsername(), cartItemRequest.getSerialNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(cartResponse.getUsername()))
                .andExpect(jsonPath("$.totalQuantity").value("0"))
                .andDo(print());
    }
}
