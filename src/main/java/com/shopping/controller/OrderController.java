package com.shopping.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @GetMapping("/orders")
    public void getOrders() {

    }

    @GetMapping("/orders/{orderId}")
    public void getOrder(@PathVariable String orderId) {

    }

    @PostMapping("/orders/{cartId}")
    public void createOrder(@PathVariable String cartId) {

    }

    @DeleteMapping("/orders/{orderId}")
    public void deleteOrder(@PathVariable String orderId) {

    }
}
