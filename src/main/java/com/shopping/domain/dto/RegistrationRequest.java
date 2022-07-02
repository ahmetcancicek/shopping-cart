package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Registration Request DTO", description = "Registration Request Data Transfer Object")
public class RegistrationRequest {

    @ApiModelProperty(value = "firstName", required = false)
    private String firstName;

    @ApiModelProperty(value = "lastName", required = false)
    private String lastName;

    @ApiModelProperty(value = "email", required = true)
    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @ApiModelProperty(value = "username", required = true)
    @Size(min = 5, message = "Username must have at least 5 characters")
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @ApiModelProperty(value = "password", required = true)
    @Size(min = 5, message = "Password must have at least 5 characters")
    @NotEmpty(message = "Password must not be empty")
    private String password;
}
