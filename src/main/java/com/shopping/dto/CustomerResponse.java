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

    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @Length(min = 5, message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be empty")
    private String username;
}
