package com.shopping.repository;

import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem,Long> {
    void deleteByCart(Cart cart);
}
