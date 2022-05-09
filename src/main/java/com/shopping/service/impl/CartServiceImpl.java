package com.shopping.service.impl;

import com.shopping.dto.CartItemRequest;
import com.shopping.dto.CartItemResponse;
import com.shopping.dto.CartResponse;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.CartMapper;
import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import com.shopping.model.Customer;
import com.shopping.model.Product;
import com.shopping.repository.CartRepository;
import com.shopping.service.CartService;
import com.shopping.service.CustomerService;
import com.shopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductService productService, CustomerService customerService) {

        this.cartRepository = cartRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Override
    public CartResponse findByUsername(String username) {
        return CartMapper.INSTANCE.fromCart(cartRepository.findByCustomer_User_Username(username).orElseThrow(() -> {
            log.error("cart does not exist with username: {}", username);
            throw new NoSuchElementFoundException(String.format("cart does not exist with username: %s", username));
        }));
    }

    @Transactional
    @Override
    public CartResponse addItemToCart(String username, String serialNumber) {
        return addItemToCart(username, serialNumber, 1);
    }

    @Transactional
    @Override
    public CartResponse addItemToCart(String username, String serialNumber, int quantity) {
        Product product = productService.findProductBySerialNumber(serialNumber);
        Customer customer = customerService.findCustomerByUsername(username);

        Cart expectedCart = cartRepository.findByCustomer_User_Username(username)
                .map((cart) -> {
                    CartItem item = cart.findItemBySerialNumber(serialNumber).map(cartItem -> {
                        cartItem.setQuantity(cartItem.getQuantity() + quantity);
                        return cartItem;
                    }).orElse(CartItem.builder()
                            .cart(cart)
                            .price(product.price)
                            .quantity(quantity)
                            .product(product)
                            .build());

                    cart.updateItem(item);
                    customer.setCart(cart);
                    cart.setCustomer(customer);

                    return cart;
                })
                .orElse(Cart.builder()
                        .customer(customer)
                        .build());

        return updateCart(expectedCart);
    }

    @Transactional
    @Override
    public CartResponse addItemToCart(String username, CartItemRequest cartItemRequest) {
        return addItemToCart(username, cartItemRequest.getSerialNumber(), cartItemRequest.getQuantity());
    }

    @Override
    public CartResponse addItemToCart(CartItemRequest cartItemRequest) {
        return addItemToCart(cartItemRequest.getUsername(), cartItemRequest.getSerialNumber(), cartItemRequest.getQuantity());
    }

    @Transactional
    @Override
    public CartResponse deleteItemFromCart(String username, String serialNumber) {
        Product product = productService.findProductBySerialNumber(serialNumber);
        Customer customer = customerService.findCustomerByUsername(username);

        Cart expectedCart = cartRepository.findByCustomer_User_Username(username)
                .map(cart -> {
                    cart.removeItem(cart.findItem(product)
                            .orElseThrow(() -> {
                                log.error("product does not exist from cart with serialNumber: {}", serialNumber);
                                throw new NoSuchElementFoundException(String.format("product does not exist from cart with serialNumber: %s", serialNumber));
                            })
                    );
                    return cart;
                })
                .orElse(Cart.builder().customer(customer).build());

        return updateCart(expectedCart);
    }

    @Override
    public CartResponse clear(String username) {
        Customer customer = customerService.findCustomerByUsername(username);
        Cart expectedCart = cartRepository.findByCustomer_User_Username(username)
                .map(cart -> {
                    cart.getItems().clear();
                    return cart;
                }).orElseGet(() -> {
                    return Cart.builder().customer(customer).build();
                });
        return updateCart(expectedCart);
    }

    @Override
    public CartResponse updateItemFromCart(CartItemRequest cartItemRequest) {
        return updateItemFromCart(cartItemRequest.getUsername(), cartItemRequest.getSerialNumber(), cartItemRequest.getQuantity());
    }

    @Override
    public CartResponse updateItemFromCart(String username, String serialNumber, int quantity) {
        Product product = productService.findProductBySerialNumber(serialNumber);
        Customer customer = customerService.findCustomerByUsername(username);

        Cart expectedCart = cartRepository.findByCustomer_User_Username(username)
                .map(cart -> {
                    cart.findItemBySerialNumber(serialNumber)
                            .map(cartItem -> {
                                cartItem.setQuantity(quantity);
                                return cartItem;
                            })
                            .orElseThrow(() -> {
                                log.error("product does not exist from cart with serial number: {}", serialNumber);
                                throw new NoSuchElementFoundException(String.format("product does not exist from cart with serial number: %s", serialNumber));
                            });

                    return cart;
                }).orElseGet(() -> Cart.builder().customer(customer).build());

        return updateCart(expectedCart);
    }

    private CartResponse updateCart(Cart cart) {
        BigDecimal totalPrice = cart.findTotalPrice();
        int totalQuantity = cart.findTotalQuantity();

        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        log.info("cart has been updated with username: {}", cart.getCustomer().getUser().getUsername());

        return toCartResponse(cart, totalPrice, totalQuantity);
    }

    // TODO: Do refactor this method to translating from entity to DTO
    private CartResponse toCartResponse(Cart cart, BigDecimal totalPrice, int totalQuantity) {
        CartResponse cartResponse = CartResponse.builder()
                .items(new HashSet<>())
                .username(cart.getCustomer().getUser().getUsername())
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .build();

        cart.getItems().forEach((item -> {
            cartResponse.getItems().add(
                    CartItemResponse
                            .builder()
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .serialNumber(item.getProduct().getSerialNumber())
                            .name(item.getProduct().getName())
                            .description(item.getProduct().getDescription())
                            .price(item.getPrice())
                            .build()
            );
        }));
        return cartResponse;
    }
}
