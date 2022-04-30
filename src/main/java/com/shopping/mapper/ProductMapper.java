package com.shopping.mapper;

import com.shopping.dto.ProductPayload;
import com.shopping.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductPayload toProductPayload(Product product);

    Product toProduct(ProductPayload productPayload);

    List<ProductPayload> toProductPayloads(List<Product> products);

    List<Product> toProducts(List<ProductPayload> productPayloads);
}
