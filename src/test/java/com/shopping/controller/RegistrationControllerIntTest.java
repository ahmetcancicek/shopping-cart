package com.shopping.controller;

import com.shopping.model.Customer;
import com.shopping.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Test
    public void it_should_register_customer() {
        // given
        User user = User.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .active(true)
                .build();

        Customer customer = Customer.builder()
                .firstName("First Name")
                .lastName("Last Name")
                .user(user)
                .build();

        // when
        ResponseEntity<Customer> postCustomer = restTemplate.exchange("/registration",
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                Customer.class);

        // then
        assertNotNull(postCustomer);
        assertEquals(postCustomer.getStatusCode(), HttpStatus.CREATED);
        assertEquals(postCustomer.getBody().getFirstName(), "First Name");

    }
}