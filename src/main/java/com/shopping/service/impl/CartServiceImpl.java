package com.shopping.service.impl;

import com.shopping.exception.ProductNotFoundException;
import com.shopping.exception.UserNotFoundException;
import com.shopping.model.*;
import com.shopping.repository.CartRepository;
import com.shopping.service.CartService;
import com.shopping.service.CustomerService;
import com.shopping.service.ProductService;
import com.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, CustomerService customerService, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Optional<Cart> findByUsername(String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            log.error("user does not exists: {}" + username);
            throw new UserNotFoundException("user does not exists: {" + username + "}");
        }

        return findByCustomer(user.get().getCustomer());
    }

    @Override
    public Optional<Cart> findByCustomerId(Long id) {
        return cartRepository.findByCustomer_Id(id);
    }


    @Override
    public Optional<Cart> findByCustomer(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }

    @Override
    public void addItemToCart(Customer customer, Product product) {
        addItemToCart(customer.getId(), product.getId(), 1);
    }


    @Override
    public void addItemToCart(Customer customer, Long productId) {
        addItemToCart(customer.getId(), productId, 1);
    }

    @Override
    public void addItemToCart(Customer customer, Product product, int quantity) {
        addItemToCart(customer.getId(), product.getId(), quantity);
    }

    @Override
    public void addItemToCart(Long customerId, Long productId, int quantity) {
        Customer customer = customerService.findById(customerId);


        Optional<Product> optionalProduct = productService.findById(productId);
        if (optionalProduct.isEmpty())
            throw new ProductNotFoundException("product does not exit with this productId");

        updateCart(customer, optionalProduct.get(), quantity);
    }

    @Override
    public void addItemToCart(Long customerId, Long productId) {
        addItemToCart(customerId, productId, 1);
    }

    @Override
    public void removeAllCartItems(Customer customer) {
        customer = customerService.findById(customer.getId());

        customer.getCart().getItems().removeIf(cartItem -> cartItem.getId() != null);

        cartRepository.save(customer.getCart());
        log.info("all items of the cart has been deleted: {}", customer.getCart().getId());
    }

    @Override
    public void removeAllCartItems(Cart cart) {
        Optional<Cart> optionalCart = cartRepository.findById(cart.getId());
        if (optionalCart.isEmpty())
            throw new IllegalArgumentException();

        cart.getItems().removeIf(cartItem -> cartItem.getId() != null);

        cartRepository.save(cart);
        log.info("all items of the cart has been deleted: {}", cart.getId());
    }

    @Override
    public void removeItem(Customer customer, Product product) {
        customer = customerService.findById(customer.getId());

        Optional<Product> optionalProduct = productService.findById(product.getId());
        if (optionalProduct.isEmpty())
            throw new ProductNotFoundException("product does not exist with this productId");

        Long productId = optionalProduct.get().getId();
        customer.getCart().getItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
    }

    private void updateCart(Customer customer, Product product, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findByCustomer(customer);

        Cart customerCart = optionalCart.map(cart -> {
            Optional<CartItem> optionalCartItem = Optional.of(cart.findItem(product).map(cartItem -> {
                cartItem.setQuantity((cartItem.getQuantity() + quantity));
                return cartItem;
            }).orElse(CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build()));

            CartItem cartItem = optionalCartItem.get();

            cart.updateItem(cartItem);
            customer.setCart(cart);
            cart.setCustomer(customer);
            return cart;
        }).orElse(Cart.builder()
                .customer(customer)
                .build());

        cartRepository.save(customerCart);
        log.info("cart has been updated: {}", customerCart.getCustomer().getCart().getId());
    }
}
