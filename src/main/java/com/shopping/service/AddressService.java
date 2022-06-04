package com.shopping.service;


import com.shopping.domain.dto.AddressRequest;
import com.shopping.domain.dto.AddressResponse;

public interface AddressService {
    AddressResponse save(String username, AddressRequest addressRequest);

    void deleteByIdAndUsername(Long id, String username);

    AddressResponse findByIdAndUsername(Long id, String username);
}
