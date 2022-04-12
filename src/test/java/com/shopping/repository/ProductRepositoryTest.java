package com.shopping.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.Validation;
import javax.validation.Validator;


@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductRepository productRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void it_should_save_product() {

    }

    @Test
    public void it_should_return_product_of_that_id() {

    }


    @Test
    public void it_should_delete_product_with_id() {

    }

    @Test
    public void it_should_return_list_of_all_products() {

    }

}
