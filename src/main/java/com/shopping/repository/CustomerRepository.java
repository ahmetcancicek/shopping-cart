package com.shopping.repository;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByUser(User user);

    Optional<Customer> findByUser_Username(String username);

    Optional<Customer> findByUser_Email(String email);

    void deleteByUser_Username(String email);
}
