package com.shopping.mapper;

import com.shopping.dto.ProductRequest;
import com.shopping.dto.ProductResponse;
import com.shopping.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductRequest productRequest);

    ProductResponse fromProduct(Product product);

    ProductRequest toProductRequestFromProduct(Product product);

    List<ProductResponse> fromProducts(List<Product> products);
}
