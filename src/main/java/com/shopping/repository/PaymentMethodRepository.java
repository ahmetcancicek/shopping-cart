package com.shopping.repository;

import com.shopping.domain.model.PaymentMethod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByIdAndCustomer_User_Username(@Param("id") Long id, @Param("username")String username);
}
