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
import java.util.Set;

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
                .customer(customer)
                .totalPrice(BigDecimal.ZERO)
                .build();

        customer.setCart(cart);

        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(customer.getCart()));

        // when
        CartResponse expectedCartResponse = cartService.findByUsername(customer.getUser().getUsername());

        // then
        verify(cartRepository, times(1)).findByCustomer_User_Username(any());
        assertNotNull(expectedCartResponse, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCartResponse.getUsername(), "Username must be equal");
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
        CartResponse expectedCartResponse = cartService.addItemToCart(customer.getUser().getUsername(), product.getSerialNumber());

        // then
        verify(cartRepository, times(1)).save(any());
        assertNotNull(expectedCartResponse, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCartResponse.getUsername(), "Username must be equal");
        assertEquals(product.getPrice(), expectedCartResponse.getTotalPrice(), "Total price must be equal");
        assertEquals(1, expectedCartResponse.getTotalQuantity(), "Total quantity must be equal");
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
        int quantity=3;
        CartResponse expectedCartResponse = cartService.addItemToCart(customer.getUser().getUsername(), product.getSerialNumber(), quantity);

        // then
        verify(cartRepository, times(1)).save(any());
        assertNotNull(expectedCartResponse, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCartResponse.getUsername(), "Username must be equal");
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(quantity)), expectedCartResponse.getTotalPrice(), "Total price must be equal");
        assertEquals(3, expectedCartResponse.getTotalQuantity(), "Total quantity must be equal");
    }

    @Test
    public void it_should_delete_item_from_cart() {
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

        Product product = Product.builder()
                .id(1L)
                .serialNumber("LKB38A97")
                .name("iPhone 15")
                .quantity(2)
                .price(BigDecimal.valueOf(1000))
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>(Set.of(
                        CartItem.builder()
                                .id(1L)
                                .price(product.getPrice())
                                .quantity(1)
                                .product(product)
                                .build())))
                .customer(customer)
                .build();

        customer.setCart(cart);

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(productService.findProductBySerialNumber(any())).willReturn(product);
        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(cart));
        given(cartRepository.save(any())).willReturn(cart);

        // when
        CartResponse expectedCartResponse = cartService.deleteItemFromCart(customer.getUser().getUsername(), product.getSerialNumber());

        // then
        verify(cartRepository, times(1)).save(any());
        assertEquals(0, cart.getItems().size(), "Size must be equal");
        assertNotNull(expectedCartResponse, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCartResponse.getUsername(), "Username must be equal");
        assertEquals(BigDecimal.ZERO, expectedCartResponse.getTotalPrice(), "Total price must be equal");
        assertEquals(0, expectedCartResponse.getTotalQuantity(), "Total quantity must be equal");
    }

    @Test
    public void it_should_clear() {
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

        Product productOne = Product.builder()
                .id(1L)
                .serialNumber("LKB38A97")
                .name("iPhone 15")
                .quantity(2)
                .price(BigDecimal.valueOf(1000))
                .build();

        Product productTwo = Product.builder()
                .id(2L)
                .serialNumber("PLN27GF")
                .name("iPhone 13")
                .quantity(1)
                .price(BigDecimal.valueOf(900))
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>(Set.of(
                        CartItem.builder()
                                .id(1L)
                                .price(productOne.getPrice())
                                .quantity(1)
                                .product(productOne)
                                .build(),
                        CartItem.builder()
                                .id(2L)
                                .price(productTwo.getPrice())
                                .quantity(1)
                                .product(productTwo)
                                .build()
                )))
                .customer(customer)
                .build();

        customer.setCart(cart);

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(cart));
        given(cartRepository.save(any())).willReturn(cart);

        // when
        CartResponse expectedCartResponse = cartService.clear(customer.getUser().getUsername());

        // then
        verify(cartRepository, times(1)).save(any());
        assertEquals(0, cart.getItems().size(), "Size must be equal");
        assertNotNull(expectedCartResponse, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCartResponse.getUsername(), "Username must be equal");
        assertEquals(BigDecimal.ZERO, expectedCartResponse.getTotalPrice(), "Total price must be equal");
        assertEquals(0, expectedCartResponse.getTotalQuantity(), "Total quantity must be equal");
    }


    @Test
    public void it_should_update_cart_items_from_cart() {
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

        Product product = Product.builder()
                .id(1L)
                .serialNumber("LKB38A97")
                .name("iPhone 15")
                .quantity(2)
                .price(BigDecimal.valueOf(1000))
                .build();

        Cart cart = Cart.builder()
                .id(1L)
                .totalPrice(BigDecimal.ZERO)
                .items(new HashSet<>(Set.of(
                        CartItem.builder()
                                .id(1L)
                                .price(product.getPrice())
                                .quantity(1)
                                .product(product)
                                .build())))
                .customer(customer)
                .build();

        customer.setCart(cart);

        given(customerService.findCustomerByUsername(any())).willReturn(customer);
        given(productService.findProductBySerialNumber(any())).willReturn(product);
        given(cartRepository.findByCustomer_User_Username(any())).willReturn(Optional.of(cart));
        given(cartRepository.save(any())).willReturn(cart);

        // when
        CartResponse expectedCartResponse = cartService.updateItemFromCart(customer.getUser().getUsername(), product.getSerialNumber(), 10);

        // then
        verify(cartRepository, times(1)).save(any());
        assertNotNull(expectedCartResponse, "Cart must not be null");
        assertEquals(customer.getUser().getUsername(), expectedCartResponse.getUsername(), "Username must be equal");
        assertEquals(product.getPrice().multiply(BigDecimal.TEN), expectedCartResponse.getTotalPrice(), "Total price must be equal");
        assertEquals(10, expectedCartResponse.getTotalQuantity(), "Total quantity must be equal");

    }
}
