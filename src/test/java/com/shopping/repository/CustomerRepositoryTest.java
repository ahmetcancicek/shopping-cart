package com.shopping.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.Validation;
import javax.validation.Validator;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void it_should_save_customer() {

    }

    @Test
    public void it_should_save_customer_with_payment_and_address() {

    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_email() {

    }

    @Test
    public void it_should_throw_exception_when_save_customer_with_existing_username() {

    }

    @Test
    public void it_should_return_cart_id_when_save_customer() {

    }

    @Test
    public void it_should_return_list_of_all_customers() {

    }

    @Test
    public void it_should_return_customer_of_that_id() {

    }

    @Test
    public void it_should_return_customer_of_that_user(){

    }
}