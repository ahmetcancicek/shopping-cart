package com.shopping.service;

import com.shopping.dto.ProductRequest;
import com.shopping.dto.ProductResponse;
import com.shopping.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse findById(Long id);

    ProductResponse findBySerialNumber(String serialNumber);

    ProductResponse save(ProductRequest productPayload);

    ProductResponse update(ProductRequest productPayload);

    void deleteById(Long id);

    void deleteBySerialNumber(String serialNumber);

    List<ProductResponse> findAll();

    Page<ProductResponse> findAll(Pageable pageable);

    Product findProductBySerialNumber(String serialNumber);
}
