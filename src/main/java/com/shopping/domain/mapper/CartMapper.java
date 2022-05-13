package com.shopping.domain.mapper;

import com.shopping.domain.dto.CartResponse;
import com.shopping.domain.model.Cart;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mappings({
            @Mapping(source = "customer.user.username", target = "username")
    })
    CartResponse fromCart(Cart cart);
}
