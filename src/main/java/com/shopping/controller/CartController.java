package com.shopping.controller;

import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.CartItemRequest;
import com.shopping.domain.dto.CartResponse;
import com.shopping.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@Api(value = "Cart API Documentation")
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "Add item to cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PostMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The item has been added successfully", cartService.addItemToCart(username, cartItemRequest)));
    }

    @ApiOperation(value = "Get cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The cart has been got successfully", cartService.findByUsername(username)));
    }

    @ApiOperation(value = "Delete item from cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @DeleteMapping("/api/cart/{serialNumber}")
    public ResponseEntity<ApiResponse<CartResponse>> deleteItemFromCart(@PathVariable String serialNumber) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The item has been deleted successfully", cartService.deleteItemFromCart(username, serialNumber)));
    }

    @ApiOperation("Delete all items from cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @DeleteMapping("/api/cart/empty")
    public ResponseEntity<ApiResponse<CartResponse>> clear() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The items has been deleted successfully", cartService.clear(username)));
    }

    @ApiOperation("Update item from cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PutMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> updateItemFromCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The items has been updated successfully", cartService.updateItemFromCart(username, cartItemRequest)));
    }
}
