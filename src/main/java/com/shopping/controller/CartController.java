package com.shopping.controller;

import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.CartItemRequest;
import com.shopping.domain.dto.CartResponse;
import com.shopping.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;


    @PostMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The item has been added successfully", cartService.addItemToCart(username, cartItemRequest)));
    }

    @PostMapping("/api/cart_bulk")
    public ResponseEntity<ApiResponse<CartResponse>> addItemsToCart(@Valid @RequestBody Set<CartItemRequest> cartItemRequestList) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The items has been added successfully", null));
    }


    @GetMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The cart has been got successfully", cartService.findByUsername(username)));
    }

    @DeleteMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> deleteItemFromCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The item has been deleted successfully", cartService.deleteItemFromCart(username, cartItemRequest.getSerialNumber())));
    }

    @DeleteMapping("/api/cart/empty")
    public ResponseEntity<ApiResponse<CartResponse>> clear() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The items has been deleted successfully", cartService.clear(username)));
    }

    @PutMapping("/api/cart")
    public ResponseEntity<ApiResponse<CartResponse>> updateItemFromCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The items has been updated successfully", cartService.updateItemFromCart(username, cartItemRequest)));
    }
}
