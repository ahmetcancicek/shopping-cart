package com.shopping.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CustomerRequest {
    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    @Length(min = 5, message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @Column(name = "password", nullable = false)
    @Length(min = 5, message = "Password must have at least 5 characters")
    @NotEmpty(message = "Password must not be empty")
    private String password;
}
