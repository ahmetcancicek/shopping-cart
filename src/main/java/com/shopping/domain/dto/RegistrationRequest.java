package com.shopping.domain.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegistrationRequest {
    private String firstName;

    private String lastName;

    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @Size(min = 5, message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @Size(min = 5, message = "Password must have at least 5 characters")
    @NotEmpty(message = "Password must not be empty")
    private String password;
}
