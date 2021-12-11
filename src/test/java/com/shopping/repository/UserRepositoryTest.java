package com.shopping.repository;

import com.shopping.model.User;
import org.junit.jupiter.api.*;
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

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("email@email.com")
                .username("username")
                .password("password")
                .active(true)
                .build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void should_save_user() {
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());

        assertTrue(fetchedUser.isPresent(), "Returned must not be null");
        assertEquals(user.getUsername(), fetchedUser.get().getUsername(), "Username must be equal");
    }

    @Test
    public void should_return_user_of_that_username_when_called_findByUsername() {
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());

        assertTrue(fetchedUser.isPresent(), "Returned must not be null");
        assertEquals(user.getUsername(), fetchedUser.get().getUsername(), "Username must be equal");
    }

    @Test
    public void should_return_user_of_that_email_when_called_findByEmail() {
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());

        assertTrue(fetchedUser.isPresent(), "Returned must not be null");
        assertEquals(user.getEmail(), fetchedUser.get().getEmail(), "Email must be equal");
    }


    @Test
    public void should_delete_user() {
        userRepository.save(user);
        userRepository.deleteById(user.getId());
    }

}