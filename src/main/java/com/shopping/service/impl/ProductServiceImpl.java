package com.shopping.service.impl;

import com.shopping.model.Product;
import com.shopping.model.User;
import com.shopping.repository.ProductRepository;
import com.shopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public Product save(Product product) {
        Product savedProduct = productRepository.save(product);
        log.info("new product has been created: {}", savedProduct.getId());

        return savedProduct;
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
        log.info("product has been deleted: {}", id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
