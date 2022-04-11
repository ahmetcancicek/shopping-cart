package com.shopping.controller;

import com.shopping.service.CartService;
import com.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CartController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }


    @GetMapping("/cart/{cartId}")
    public void getCart(@PathVariable String cartId) {

    }

    @PostMapping("/cart")
    public void addItemToCart() {

    }

    @DeleteMapping("/cart/{itemId}")
    public void deleteItemFromCart(@PathVariable String itemId) {

    }

    @PatchMapping("/cart/{itemId}")
    public void updateItemFromCart(@PathVariable String itemId) {

    }

    @DeleteMapping("/cart/empty")
    public void clear() {

    }

    @PostMapping("/cart/{cartId}/checkout")
    public void checkout(@PathVariable String cartId) {

    }

}
