package com.shopping.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Address Request DTO", description = "Address Request Data Transfer Object")
public class AddressRequest {

    @ApiModelProperty(value = "street", required = true)
    @Length(min = 3, message = "Street must have at least 3 characters")
    @NotEmpty(message = "Street must not be empty")
    private String street;

    @ApiModelProperty(value = "city", required = true)
    @Length(min = 3, message = "City must have at least 3 characters")
    @NotEmpty(message = "City must not be empty")
    private String city;

    @ApiModelProperty(value = "zipCode", required = true)
    @NotEmpty(message = "Zip Code must not be empty")
    @Length(min = 3, message = "Zip Code  must have at least 3 characters")
    private String zipCode;

    @ApiModelProperty(value = "stateCode", required = true)
    @NotEmpty(message = "State Code must not be empty")
    @Length(min = 2, max = 2, message = "State Code must have 2 characters")
    private String stateCode;
}
