package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.domain.dto.CartItemRequest;
import com.shopping.domain.dto.CartItemResponse;
import com.shopping.domain.dto.CartResponse;
import com.shopping.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
@AutoConfigureMockMvc
public class CartControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;
    @Autowired
    private ObjectMapper mapper;

    private CartItemRequest cartItemRequest;
    private CartResponse cartResponse;


    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    @Test
    public void it_should_add_item_to_cart() throws Exception {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .serialNumber("Y5N3DJ")
                .quantity(1)
                .build();

        CartResponse cartResponse = CartResponse.builder()
                .username("stevehouse")
                .totalQuantity(1)
                .totalPrice(BigDecimal.TEN)
                .items(new HashSet<>(Set.of(CartItemResponse.builder()
                        .price(BigDecimal.TEN)
                        .name("Egg")
                        .description("Super Egg")
                        .quantity(1)
                        .serialNumber("Y5N3DJ")
                        .build())))
                .build();

        given(cartService.addItemToCart(any(), any())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartItemRequest));

        // then
        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.totalQuantity").value(1))
                .andExpect(jsonPath("$.data.username").value(cartResponse.getUsername()))
                .andExpect(jsonPath("$.data.totalPrice").value("10"))
                .andExpect(jsonPath("$.data.items[0].serialNumber").value("Y5N3DJ"))
                .andExpect(jsonPath("$.data.items[0].name").value("Egg"))
                .andExpect(jsonPath("$.data.items[0].quantity").value("1"))
                .andExpect(jsonPath("$.data.items[0].description").value("Super Egg"))
                .andExpect(jsonPath("$.data.items[0].price").value("10"));
    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    @Test
    public void it_should_return_cart_of_that_customer() throws Exception {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .serialNumber("Y5N3DJ")
                .quantity(1)
                .build();

        CartResponse cartResponse = CartResponse.builder()
                .username("stevehouse")
                .totalQuantity(1)
                .totalPrice(BigDecimal.TEN)
                .items(new HashSet<>(Set.of(CartItemResponse.builder()
                        .price(BigDecimal.TEN)
                        .name("Egg")
                        .description("Super Egg")
                        .quantity(1)
                        .serialNumber("Y5N3DJ")
                        .build())))
                .build();

        given(cartService.findByUsername(any())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartItemRequest));

        // then
        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.totalQuantity").value(1))
                .andExpect(jsonPath("$.data.username").value(cartResponse.getUsername()))
                .andExpect(jsonPath("$.data.totalPrice").value("10"))
                .andExpect(jsonPath("$.data.items[0].serialNumber").value("Y5N3DJ"))
                .andExpect(jsonPath("$.data.items[0].name").value("Egg"))
                .andExpect(jsonPath("$.data.items[0].quantity").value("1"))
                .andExpect(jsonPath("$.data.items[0].description").value("Super Egg"))
                .andExpect(jsonPath("$.data.items[0].price").value("10"));
    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    @Test
    public void it_should_delete_item_from_cart() throws Exception {
        // given
        CartResponse cartResponse = CartResponse.builder()
                .username("stevehouse")
                .totalQuantity(0)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>())
                .build();

        given(cartService.deleteItemFromCart(any(), any())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/cart/{serialNumber}", "Y5N3DJ")
                .contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.totalQuantity").value(0))
                .andExpect(jsonPath("$.data.username").value(cartResponse.getUsername()))
                .andExpect(jsonPath("$.data.totalPrice").value("0"));
    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    @Test
    public void it_should_delete_all_items_from_cart() throws Exception {
        // given
        CartResponse cartResponse = CartResponse.builder()
                .username("stevehouse")
                .totalQuantity(0)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>())
                .build();

        given(cartService.clear(any())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/api/v1/cart/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.totalQuantity").value(0))
                .andExpect(jsonPath("$.data.username").value(cartResponse.getUsername()))
                .andExpect(jsonPath("$.data.totalPrice").value("0"));
    }

    @WithMockUser(username = "stevehouse", password = "GT380ABD")
    @Test
    public void it_should_update_item_from_cart() throws Exception {
        // given
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .serialNumber("Y5N3DJ")
                .quantity(1)
                .build();

        CartResponse cartResponse = CartResponse.builder()
                .username("stevehouse")
                .totalQuantity(1)
                .totalPrice(BigDecimal.TEN)
                .items(new HashSet<>(Set.of(CartItemResponse.builder()
                        .price(BigDecimal.TEN)
                        .name("Egg")
                        .description("Super Egg")
                        .quantity(1)
                        .serialNumber("Y5N3DJ")
                        .build())))
                .build();

        given(cartService.updateItemFromCart(any(), any())).willReturn(cartResponse);

        // when
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/v1/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cartItemRequest));

        // then
        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.totalQuantity").value(1))
                .andExpect(jsonPath("$.data.username").value(cartResponse.getUsername()))
                .andExpect(jsonPath("$.data.totalPrice").value("10"))
                .andExpect(jsonPath("$.data.items[0].serialNumber").value("Y5N3DJ"))
                .andExpect(jsonPath("$.data.items[0].name").value("Egg"))
                .andExpect(jsonPath("$.data.items[0].quantity").value("1"))
                .andExpect(jsonPath("$.data.items[0].description").value("Super Egg"))
                .andExpect(jsonPath("$.data.items[0].price").value("10"));
    }
}
