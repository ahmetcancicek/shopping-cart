package com.shopping.domain.mapper;

import com.shopping.domain.dto.AddressRequest;
import com.shopping.domain.dto.AddressResponse;
import com.shopping.domain.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toAddress(AddressRequest addressRequest);

    AddressResponse fromAddress(Address address);
}
