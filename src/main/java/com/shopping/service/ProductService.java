package com.shopping.service;

import com.shopping.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);
}
