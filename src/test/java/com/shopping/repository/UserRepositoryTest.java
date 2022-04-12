package com.shopping.repository;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.Validation;
import javax.validation.Validator;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void it_should_save_user() {

    }

    @Test
    public void it_should_return_user_of_that_username() {

    }

    @Test
    public void it_should_return_user_of_that_email() {

    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_email() {

    }

    @Test
    public void it_should_throw_exception_when_save_user_with_existing_username() {

    }

    @Test
    public void it_should_delete_user() {

    }
}