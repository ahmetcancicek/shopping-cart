package com.shopping.integration.repository;

import com.shopping.common.EcommerceStarterMySQLApplicationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractIT {

    @Autowired
    protected TestEntityManager testEntityManager;

    @Container
    private static EcommerceStarterMySQLApplicationContainer container = EcommerceStarterMySQLApplicationContainer.getInstance();
}
