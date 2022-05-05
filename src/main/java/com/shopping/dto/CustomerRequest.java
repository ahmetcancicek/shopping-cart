package com.shopping.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
