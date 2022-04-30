package com.shopping.service;

import com.shopping.dto.ProductPayload;
import com.shopping.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductPayload findById(Long id);

    ProductPayload findBySerialNumber(String serialNumber);

    ProductPayload save(ProductPayload productPayload);

    ProductPayload update(ProductPayload productPayload);

    void deleteById(Long id);

    void deleteBySerialNumber(String serialNumber);

    List<ProductPayload> findAll();

    Page<ProductPayload> findAll(Pageable pageable);
}
