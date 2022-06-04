package com.shopping.controller;

import com.shopping.domain.dto.ApiResponse;
import com.shopping.domain.dto.PaymentMethodRequest;
import com.shopping.domain.dto.PaymentMethodResponse;
import com.shopping.service.PaymentMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@Api(value = "Payment Method API Documentation")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @ApiOperation(value = "Add payment")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/paymentmethod")
    public ResponseEntity<ApiResponse<PaymentMethodResponse>> addPaymentMethod(@Valid @RequestBody PaymentMethodRequest paymentMethodRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "The payment method has added successfully", paymentMethodService.save(username, paymentMethodRequest)));
    }

    @ApiOperation(value = "Delete payment")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/api/paymentmethod/{id}")
    public ResponseEntity<ApiResponse<String>> deletePaymentMethod(@PathVariable String id) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        paymentMethodService.deleteByIdAndUsername(Long.valueOf(id), username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The payment method has been deleted successfully", null));
    }

    @ApiOperation(value = "Get payment")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/paymentmethod/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodResponse>> getPaymentMethod(@PathVariable String id) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The payment method has been got successfully", paymentMethodService.findByIdAndUsername(Long.valueOf(id), username)));
    }
}
