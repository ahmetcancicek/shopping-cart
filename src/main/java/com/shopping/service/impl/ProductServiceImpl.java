package com.shopping.service.impl;

import com.shopping.dto.ProductRequest;
import com.shopping.dto.ProductResponse;
import com.shopping.exception.AlreadyExistsElementException;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.ProductMapper;
import com.shopping.model.Product;
import com.shopping.repository.ProductRepository;
import com.shopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse findById(Long id) {
        return ProductMapper.INSTANCE.fromProduct(
                productRepository.findById(id).orElseThrow(() -> {
                    log.error("product does not exist with id: {}", id);
                    return new NoSuchElementFoundException(String.format("product does not exist with id: {%d}", id));
                }));
    }

    @Override
    public ProductResponse findBySerialNumber(String serialNumber) {
        return ProductMapper.INSTANCE.fromProduct(
                productRepository.findBySerialNumber(serialNumber).orElseThrow(() -> {
                    log.error("product does not exist with serialNumber: {}", serialNumber);
                    return new NoSuchElementFoundException(String.format("product does not exist with serialNumber: {%s}", serialNumber));
                })
        );
    }

    @Transactional
    @Override
    public ProductResponse save(ProductRequest productPayload) {
        productRepository.findBySerialNumber(productPayload.getSerialNumber())
                .ifPresent((it) -> {
                    log.error("product already exists with serialNumber: {}", it.getSerialNumber());
                    throw new AlreadyExistsElementException(String.format("product already exist with serial number: {%s}", it.getSerialNumber()));
                });

        ProductResponse savedProduct = ProductMapper.INSTANCE.fromProduct(
                productRepository.save(ProductMapper.INSTANCE.toProduct(productPayload))
        );

        log.info("new product has been created: {}", savedProduct);
        return savedProduct;
    }

    @Override
    public ProductResponse update(ProductRequest productPayload) {
        Product product = productRepository.findBySerialNumber(productPayload.getSerialNumber()).orElseThrow(() -> {
            log.error("product does not exist with serialNumber: {}", productPayload.getSerialNumber());
            return new NoSuchElementFoundException(String.format("product does not exist with serialNumber: {%s}", productPayload.getSerialNumber()));
        });

        // TODO: Do refactor here
        product.setSerialNumber(productPayload.getSerialNumber());
        product.setPrice(productPayload.getPrice());
        product.setDescription(productPayload.getDescription());
        product.setName(productPayload.getName());
        product.setQuantity(productPayload.getQuantity());

        Product updatedProduct = productRepository.save(product);

        log.info("product has ben updated: {}", updatedProduct.toString());
        return ProductMapper.INSTANCE.fromProduct(updatedProduct);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        ProductResponse productResponse = findById(id);
        productRepository.deleteById(id);
        log.info("product has been deleted: {}", productResponse.toString());
    }

    @Transactional
    @Override
    public void deleteBySerialNumber(String serialNumber) {
        ProductResponse productResponse = findBySerialNumber(serialNumber);
        productRepository.deleteBySerialNumber(serialNumber);
        log.info("product has been delete: {}", productResponse.toString());
    }

    @Override
    public List<ProductResponse> findAll() {
        return ProductMapper.INSTANCE.fromProducts(productRepository.findAll());
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        // TODO: Fix findAll test for product
        return null;
    }
}
