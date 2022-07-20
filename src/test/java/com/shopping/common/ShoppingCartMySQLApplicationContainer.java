package com.shopping.common;

import org.testcontainers.containers.MySQLContainer;

public class ShoppingCartMySQLApplicationContainer extends MySQLContainer<ShoppingCartMySQLApplicationContainer> {
    private static final String IMAGE_VERSION = "mysql:8.0.28";
    private static ShoppingCartMySQLApplicationContainer container;

    private ShoppingCartMySQLApplicationContainer() {
        super(IMAGE_VERSION);
    }

    public static ShoppingCartMySQLApplicationContainer getInstance() {
        if (container == null) {
            container = new ShoppingCartMySQLApplicationContainer();
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
