package com.shopping.service.impl;

import com.shopping.dto.CartResponse;
import com.shopping.exception.NoSuchElementFoundException;
import com.shopping.model.*;
import com.shopping.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private ProductServiceImpl productService;

    @Test
    public void it_should_return_cart_of_that_username_of_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .customer(customer)
                .build();

        customer.setCart(cart);

        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(cart));

        // when
        CartResponse expectedCart = cartService.findByUsername(customer.getUser().getUsername());

        // then
        verify(cartRepository, times(1)).findByCustomer_User_Username(any());
        assertNotNull(expectedCart, "Cart must not be null");
        assertEquals(cart.getCustomer().getUser().getUsername(), expectedCart.getUsername(), "Username must be equal");
    }

    @Test
    public void it_should_throw_exception_when_does_not_exist_that_customer() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .customer(customer)
                .build();

        customer.setCart(cart);

        // when
        Throwable throwable = catchThrowable(() -> {
            cartService.findByUsername(customer.getUser().getUsername());
        });

        // then
        assertThat(throwable).isInstanceOf(NoSuchElementFoundException.class);
    }

    @Test
    public void it_should_add_item_to_cart_when_cart_is_empty() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>())
                .customer(customer)
                .build();

        customer.setCart(cart);

        Product product = Product.builder()
                .id(1L)
                .serialNumber("LKB38A97")
                .name("iPhone 15")
                .quantity(2)
                .price(BigDecimal.valueOf(1000))
                .build();

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(productService.findProductBySerialNumber(any())).willReturn(product);
        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(cart));
        given(cartRepository.save(any())).willReturn(cart);


        // when
        CartResponse expectedCart = cartService.addItemToCart(customer.getUser().getUsername(), product.getSerialNumber());

        // then
        assertNotNull(expectedCart, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCart.getUsername(), "Username must be equal");
        assertEquals(product.getPrice(), expectedCart.getTotalPrice(), "Total price must be equal");
        assertEquals(1, expectedCart.getTotalQuantity(), "Total quantity must be equal");
    }

    @Test
    public void it_should_add_item_to_cart_with_quantity_when_cart_is_empty() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Lucy")
                .lastName("King")
                .user(User.builder()
                        .id(1L)
                        .active(true)
                        .username("lucyking")
                        .email("lucyking@email.com")
                        .password("LA4SD12")
                        .build())
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>())
                .customer(customer)
                .build();

        customer.setCart(cart);

        Product product = Product.builder()
                .id(1L)
                .serialNumber("LKB38A97")
                .name("iPhone 15")
                .quantity(2)
                .price(BigDecimal.valueOf(1000))
                .build();

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(productService.findProductBySerialNumber(any())).willReturn(product);
        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(cart));
        given(cartRepository.save(any())).willReturn(cart);

        // when
        CartResponse expectedCart = cartService.addItemToCart(customer.getUser().getUsername(), product.getSerialNumber(), 3);

        // then
        assertNotNull(expectedCart, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCart.getUsername(), "Username must be equal");
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(3)), expectedCart.getTotalPrice(), "Total price must be equal");
        assertEquals(3, expectedCart.getTotalQuantity(), "Total quantity must be equal");
    }
}
