package com.shopping.service.impl;

import com.shopping.model.*;
import com.shopping.repository.CartRepository;
import com.shopping.service.CustomerService;
import com.shopping.service.ProductService;
import com.shopping.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Customer customer;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("username")
                .email("email@email.com")
                .active(true)
                .password("password")
                .build();

        customer = Customer.builder()
                .id(2L)
                .user(user)
                .firstName("First Name")
                .lastName("Last Name")
                .build();

        product = Product.builder()
                .name("Product")
                .quantity(10)
                .price(BigDecimal.valueOf(10))
                .id(10L)
                .build();

        cart = Cart.builder()
                .id(5L)
                .customer(customer)
                .build();

        CartItem cartItem = CartItem.builder()
                .id(3L)
                .cart(cart)
                .product(product)
                .quantity(1)
                .price(BigDecimal.valueOf(10))
                .build();

        cart.addItem(cartItem);
        user.setCustomer(customer);
        customer.setCart(cart);
    }

    @Test
    void it_should_return_cart_of_that_username() {
        // given
        String username = user.getUsername();
        when(userService.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomer(any(Customer.class))).thenReturn(Optional.of(cart));

        // when
        Optional<Cart> expectedCart = cartService.findByUsername(username);

        // then
        verify(cartRepository, times(1)).findByCustomer(any(Customer.class));
        assertTrue(expectedCart.isPresent(), "Returned must no be null");
    }

    @Test
    void it_should_return_cart_of_that_customer_id() {
        // given
        Long customerId = customer.getId();
        Long cartId = cart.getId();
        when(cartRepository.findByCustomer_Id(any())).thenReturn(Optional.of(cart));

        // when
        Optional<Cart> expectedCart = cartService.findByCustomerId(customerId);

        // then
        verify(cartRepository, times(1)).findByCustomer_Id(customerId);
        assertTrue(expectedCart.isPresent(), "Returned must not be null");
        assertEquals(expectedCart.get().getId(), cartId);
    }

    @Test
    void it_should_return_cart_of_that_customer() {
        // given
        Long cartId = 5L;
        when(cartRepository.findByCustomer(any(Customer.class))).thenReturn(Optional.of(cart));

        // when
        Optional<Cart> expectedCart = cartService.findByCustomer(customer);

        // then
        verify(cartRepository, times(1)).findByCustomer(customer);
        assertTrue(expectedCart.isPresent(), "Returned must not be null");
        assertEquals(expectedCart.get().getId(), cartId, "CartId must be equal");
    }

    @Test
    void it_should_add_item_to_cart_with_customer_and_product() {
        // given
        Long customerId = customer.getId();
        Long productId = product.getId();
        when(customerService.findById(any())).thenReturn(customer);
        when(productService.findById(any())).thenReturn(Optional.of(product));
        when(cartRepository.save(any())).thenReturn(cart);
        when(cartRepository.findByCustomer(any())).thenReturn(Optional.of(cart));

        // when
        cartService.addItemToCart(customer, product);

        // then
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(cartRepository, times(1)).findByCustomer(any(Customer.class));
    }


    @Test
    void it_should_remove_all_cart_items_with_customer() {
        // given
        when(customerService.findById(any())).thenReturn(customer);

        // when
        cartService.removeAllCartItems(customer);

        // then
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertEquals(0, customer.getCart().getItems().size());
    }

    @Test
    void it_should_remove_all_cart_items_with_cart() {
        // given
        when(cartRepository.findById(any())).thenReturn(Optional.of(cart));

        // when
        cartService.removeAllCartItems(cart);

        // then
        verify(cartRepository, times(1)).save(any());
        assertEquals(0, customer.getCart().getItems().size());
    }

    @Test
    void it_should_remove_item_from_cart() {
        // given
        when(customerService.findById(any())).thenReturn(customer);
        when(productService.findById(any())).thenReturn(Optional.ofNullable(product));

        // when
        cartService.removeItem(customer, product);

        // then
        assertEquals(0, customer.getCart().getItems().size());

    }

    @Test
    void it_should_add_item_with_the_number_of_items() {
        // TODO:
    }

    @Test
    void it_should_increase_the_count_of_the_cart_items_when_adds_the_same_product() {
        // TODO:
    }

    @Test
    void it_should_decrease_the_count_of_that_cart_items_when_exist_items() {
        // TODO:
    }

    @Test
    void it_should_throw_error_when_add_item_to_cart_with_does_not_exist_customer() {
        // TODO:
    }

    @Test
    void it_should_throw_error_when_add_item_to_cart_with_does_not_exist_product() {
        // TODO:
    }
}
