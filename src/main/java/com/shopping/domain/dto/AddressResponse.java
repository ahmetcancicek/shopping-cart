package com.shopping.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponse {
    private Long id;
    private String street;
    private String city;
    private String zipCode;
    private String stateCode;
}
