package com.shopping.servivce.impl;

import com.shopping.model.Product;
import com.shopping.repository.ProductRepository;
import com.shopping.servivce.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> findAllProductsPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
