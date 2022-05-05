package com.shopping.service.impl;

import com.shopping.dto.CartItemResponse;
import com.shopping.dto.CartResponse;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.mapper.CartMapper;
import com.shopping.model.Cart;
import com.shopping.model.CartItem;
import com.shopping.model.Customer;
import com.shopping.model.Product;
import com.shopping.repository.CartRepository;
import com.shopping.repository.ProductRepository;
import com.shopping.service.CartService;
import com.shopping.service.CustomerService;
import com.shopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


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
            throw new NoSuchElementFoundException(String.format("cart does not exist with username: {%s}", username));
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


        Cart cart = cartRepository.save(expectedCart);
        log.info("cart has been updated with username: {}", username);

        // TODO: Do refactored this codes
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;
        Set<CartItem> cartItems = cart.getItems();
        for (CartItem item : cartItems) {
            totalPrice = totalPrice.add(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity()));
            totalQuantity += item.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cartRepository.save(expectedCart);


        CartResponse cartResponse = CartResponse.builder()
                .items(new HashSet<>())
                .username(username)
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
