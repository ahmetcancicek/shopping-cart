package com.shopping.controller;

import com.shopping.domain.dto.AddressRequest;
import com.shopping.domain.dto.AddressResponse;
import com.shopping.domain.dto.ApiResponse;
import com.shopping.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_USER')")
@Api(value = "Address API Documentation")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @ApiOperation(value = "Add address")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(@Valid @RequestBody AddressRequest addressRequest) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "The address has been added successfully", addressService.save(username, addressRequest)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete address")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable String id) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        addressService.deleteByIdAndUsername(Long.valueOf(id), username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The address has been deleted successfully", null));

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get address")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(@PathVariable String id) {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(), "The address has been got successfully", addressService.findByIdAndUsername(Long.valueOf(id), username)));
    }
}
