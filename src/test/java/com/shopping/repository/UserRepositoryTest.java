package com.shopping.repository;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .users(user)
                .build();

        user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .customer(customer)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        user = null;
        customer = null;
    }

    @Test
    public void should_save_user() {
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());

        assertTrue(fetchedUser.isPresent());

        assertEquals(user.getUsername(), fetchedUser.get().getUsername());
    }

    @Test
    public void should_return_user_of_that_username_when_called_findByUsername() {
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());

        assertTrue(fetchedUser.isPresent());

        assertEquals(user.getUsername(), fetchedUser.get().getUsername());
    }

    @Test
    public void should_return_user_of_that_email_when_called_findByEmail() {
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());

        assertTrue(fetchedUser.isPresent());

        assertEquals(user.getEmail(), fetchedUser.get().getEmail());
    }


    @Test
    public void should_delete_user() {
        userRepository.save(user);

        userRepository.deleteById(user.getId());
    }

}