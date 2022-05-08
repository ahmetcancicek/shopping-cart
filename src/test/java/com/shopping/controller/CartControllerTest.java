package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.dto.CartItemRequest;
import com.shopping.dto.CartItemResponse;
import com.shopping.dto.CartResponse;
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
                .items(new HashSet<>())
                .build();

        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .price(BigDecimal.TEN)
                .name("Egg")
                .description("Super egg")
                .quantity(1)
                .serialNumber("Y5N3DJ")
                .build();

        cartResponse.getItems().add(cartItemResponse);

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
                .items(new HashSet<>())
                .build();

        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .price(BigDecimal.TEN)
                .name("Egg")
                .description("Super egg")
                .quantity(1)
                .serialNumber("Y5N3DJ")
                .build();

        cartResponse.getItems().add(cartItemResponse);

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
    public void it_should_delete_item_of_that_id_from_cart() {
        // given

        // when

        // then
    }


    @Test
    public void it_should_update_item_of_that_id_from_cart() {
        // given

        // when

        // then
    }

    @Test
    public void it_should_clear_cart() {
        // given

        // when

        // then
    }
}
