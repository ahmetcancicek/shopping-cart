package com.shopping.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CustomerResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
