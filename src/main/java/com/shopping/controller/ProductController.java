package com.shopping.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @GetMapping("/products")
    public void getProducts() {

    }

    @GetMapping("/products/{productId}")
    public void getProduct(@PathVariable String productId) {

    }

    @PostMapping("/products")
    public void addProduct() {

    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@PathVariable String productId) {

    }

    @PatchMapping("/products/{productId}")
    public void updateProduct(@PathVariable String productId) {

    }
}
