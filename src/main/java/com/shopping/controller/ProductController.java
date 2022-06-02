package com.shopping.controller;

import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.ProductRequest;
import com.shopping.domain.dto.ProductResponse;
import com.shopping.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@AllArgsConstructor
@RestController
@Api(value = "Product API Documentation")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/products")
    @ApiOperation(value = "Get products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The product has been got successfully.", productService.findAll()));
    }

    @GetMapping("/api/products/{serialNumber}")
    @ApiOperation(value = "Get product")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable String serialNumber) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The products has been got successfully.", productService.findBySerialNumber(serialNumber)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/products")
    @ApiOperation(value = "Add product")
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "The product has been added successfully.", productService.save(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/products/{serialNumber}")
    @ApiOperation(value = "Delete product")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable String serialNumber) {
        productService.deleteBySerialNumber(serialNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The product has been deleted successfully.", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/products")
    @ApiOperation(value = "Update product")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The product has been updated successfully.", productService.update(request)));
    }
}
