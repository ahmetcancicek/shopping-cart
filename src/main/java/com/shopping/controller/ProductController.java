package com.shopping.controller;

import com.shopping.dto.ProductPayload;
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
    public List<ProductPayload> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/products/{serialNumber}")
    public ProductPayload getProduct(@PathVariable String serialNumber) {
        return productService.findBySerialNumber(serialNumber);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products")
    public ProductPayload addProduct(@Valid @RequestBody ProductPayload productPayload) {
        return productService.save(productPayload);
    }

    @DeleteMapping("/products/{serialNumber}")
    public void deleteProduct(@PathVariable String serialNumber) {
        productService.deleteBySerialNumber(serialNumber);
    }

    @PutMapping("/products")
    public ProductPayload updateProduct(@Valid @RequestBody ProductPayload productPayload) {
        return productService.update(productPayload);
    }
}
