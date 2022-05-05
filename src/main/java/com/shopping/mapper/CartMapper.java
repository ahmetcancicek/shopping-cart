package com.shopping.mapper;

import com.shopping.dto.CartResponse;
import com.shopping.model.Cart;
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
