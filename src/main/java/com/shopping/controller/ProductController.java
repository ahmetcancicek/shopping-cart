package com.shopping.controller;

import com.shopping.model.Product;
import com.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.findById(Long.valueOf(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products")
    public Product addProduct(@Valid @RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteById(Long.valueOf(id));
    }

    @PutMapping("/products")
    public Product updateProduct(@Valid @RequestBody Product product) {
        return productService.update(product);
    }
}
