package com.shopping.service.impl;

import com.shopping.dto.ProductPayload;
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
    public ProductPayload findById(Long id) {
        return ProductMapper.INSTANCE.toProductPayload(
                productRepository.findById(id).orElseThrow(() -> {
                    log.error("product does not exist with id: {}", id);
                    return new NoSuchElementFoundException(String.format("product does not exist with id: {%d}", id));
                }));
    }

    @Override
    public ProductPayload findBySerialNumber(String serialNumber) {
        return ProductMapper.INSTANCE.toProductPayload(
                productRepository.findBySerialNumber(serialNumber).orElseThrow(() -> {
                    log.error("product does not exist with serialNumber: {}", serialNumber);
                    return new NoSuchElementFoundException(String.format("product does not exist with serialNumber: {%s}", serialNumber));
                })
        );
    }

    @Transactional
    @Override
    public ProductPayload save(ProductPayload productPayload) {
        productRepository.findBySerialNumber(productPayload.getSerialNumber())
                .ifPresent((it) -> {
                    log.error("product already exists with serialNumber: {}", it.getSerialNumber());
                    throw new AlreadyExistsElementException(String.format("product already exist with serial number: {%s}", it.getSerialNumber()));
                });

        ProductPayload savedProduct = ProductMapper.INSTANCE.toProductPayload(
                productRepository.save(ProductMapper.INSTANCE.toProduct(productPayload))
        );

        log.info("new product has been created: {}", savedProduct);
        return savedProduct;
    }

    @Override
    public ProductPayload update(ProductPayload productPayload) {
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
        return ProductMapper.INSTANCE.toProductPayload(updatedProduct);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        ProductPayload productPayload = findById(id);
        productRepository.deleteById(id);
        log.info("product has been deleted: {}", productPayload.toString());
    }

    @Transactional
    @Override
    public void deleteBySerialNumber(String serialNumber) {
        ProductPayload productPayload = findBySerialNumber(serialNumber);
        productRepository.deleteBySerialNumber(serialNumber);
        log.info("product has been delete: {}", productPayload.toString());
    }

    @Override
    public List<ProductPayload> findAll() {
        return ProductMapper.INSTANCE.toProductPayloads(productRepository.findAll());
    }

    @Override
    public Page<ProductPayload> findAll(Pageable pageable) {
        // TODO: Fix findAll test for product
        return null;
    }
}
