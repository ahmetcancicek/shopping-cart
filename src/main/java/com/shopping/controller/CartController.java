package com.shopping.controller;

import com.shopping.dto.CartItemRequest;
import com.shopping.dto.CartResponse;
import com.shopping.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/carts")
    public CartResponse addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        return cartService.addItemToCart(cartItemRequest);
    }

    @GetMapping("/carts/{username}")
    public CartResponse getCart(@PathVariable String username) {
        return cartService.findByUsername(username);
    }

    @DeleteMapping("/carts/{username}")
    public CartResponse deleteItemFromCart(@PathVariable String username, @RequestParam("serialNumber") String serialNumber) {
        return cartService.deleteItemFromCart(username, serialNumber);
    }
}
