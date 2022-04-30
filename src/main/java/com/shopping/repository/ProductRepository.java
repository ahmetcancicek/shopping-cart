package com.shopping.repository;

import com.shopping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySerialNumber(String serialNumber);

    void deleteBySerialNumber(@Param("serialNumber") String serialNumber);
}
