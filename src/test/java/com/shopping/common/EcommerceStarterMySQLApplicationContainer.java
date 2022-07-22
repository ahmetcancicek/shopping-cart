package com.shopping.common;

import org.testcontainers.containers.MySQLContainer;

public class EcommerceStarterMySQLApplicationContainer extends MySQLContainer<EcommerceStarterMySQLApplicationContainer> {
    private static final String IMAGE_VERSION = "mysql:8.0.28";
    private static EcommerceStarterMySQLApplicationContainer container;

    private EcommerceStarterMySQLApplicationContainer() {
        super(IMAGE_VERSION);
    }

    public static EcommerceStarterMySQLApplicationContainer getInstance() {
        if (container == null) {
            container = new EcommerceStarterMySQLApplicationContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {

    }
}
