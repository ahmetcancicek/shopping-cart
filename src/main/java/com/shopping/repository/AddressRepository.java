package com.shopping.repository;

import com.shopping.domain.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    Optional<Address> findByIdAndCustomer_User_Username(@Param("id") Long id, @Param("username") String username);
}
