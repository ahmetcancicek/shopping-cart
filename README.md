# Shopping Cart

This repository contains a basic sample application to serve restful service with JWT token authentication in a spring boot application. The application uses maven as a build tool and Docker as a containers 

# Technology

1. Spring Boot (2.4.12)
2. JWT (0.11.5)
3. MySQL
4. Java 11
5. Map Struct (1.4.2.Final)
6. Testcontainers (1.16.3)
7. Docker

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)

## Development

To test the application

```bash
./mvnw test
```

To build and run the application

```bash
docker-compose up -d shoppingcart-mysql
./mvnw spring-boot:run
```

## Deployment

To deploy this project run

```bash
  docker-compose up -d
```

## API Routes

### [Authentication API](#1-authentication-api)

| API                                                | Description         |
|:---------------------------------------------------|:--------------------|
| [`POST/api/auth/register`](#a-register-a-new-user) | Register a new user |
| [`POST/api/auth/login`](#b-login-a-user)           | Login a user        |


### [User API](#2-user-api)

| API                                                                           | Description                                            |
|:------------------------------------------------------------------------------|:-------------------------------------------------------|
| [`GET/api/users/:username`](#a-get-details-of-a-user)                         | Get details of a particular user that had a login      |
| [`GET/api/users/:username/addresses`](#b-get-list-of-all-addresses-of-a-user) | Get the list of all addresses of user that had a login |
| [`GET/api/users/:username/payments`](#b-get-list-of-all-payments-of-a-user)   | Get the list of all payments of user that had a login  |


### [Product API](#3-product-api)

| API                                                                  | Description                      |
|:---------------------------------------------------------------------|:---------------------------------|
| [`POST/api/products`](#b-post-a-new-product)                         | Create a new product             |
| [`GET/api/products`](#b-get-list-of-all-products)                    | Get list of all products         |
| [`GET/api/products/:serialNumber`](#b-get-details-of-a-product)      | Get details a particular product |
| [`PATCH/api/products/:serialNumber`](#b-update-details-of-a-product) | Update a particular product      |
| [`DELETE/api/products/:serialNumber`](#b-delete-a-product)           | Delete a particular product      |


### [Cart API](#4-cart-api)

| API                                      | Description                                   |
|:-----------------------------------------|:----------------------------------------------|
| [`GET/api/carts`](#b-post-a-new-product) | Get list of all items that belong to customer |


### [Order API](#45-order-api)

| API                                                        | Description                                    |
|:-----------------------------------------------------------|:-----------------------------------------------|
| [`POST/api/orders`](#b-post-a-new-order)                   | Create a new order                             |
| [`GET/api/orders`](#b-get-list-of-all-orders)              | Get list of all orders that belong to customer |
| [`GET/api/orders/:orderNumber`](#b-get-details-of-a-order) | Get details of a particular oder               |
| [`GET/api/orders/:orderNumber`](#b-get-delete-a-order)     | Delete a particular oder                       |


## 1. Authencation API

### a. Register a new user

You can send a POST request to register a new user and returns a web token for authentication

```
Method: POST
URL: /api/auth/register
Produces: application/json
```

#### Parameters : 

| Parameter    | Type      | Description                           |
|:-------------|:----------|:--------------------------------------|
| `firstName`  | `string`  | **Not Required**. Name of customer    |
| `lastName`   | `string`  | **Not Required**. Surname of customer |
| `email`      | `string`  | **Required**. Email of customer       |
| `username`   | `string`  | **Required**. Username of customer    |
| `password`   | `string`  | **Required**. Password of customer    |

#### Example :

* Request : 

```curl
curl -X POST 'localhost:8090/api/auth/register/' \
-H 'Content-Type: application/json' \
-d '{"username":"stevekey","password":"KHG279BD","email":"stevekey@email.com","firstname":"Steve","lastname":"Key"}'
```

* Response : 

```json

```

### b. Login a user

You can send a POST request to login a user and returns a web token for authentication

```
Method: POST
URL: /api/auth/login
Produces: application/json
```

#### Parameters :

| Parameter    | Type      | Description                           |
|:-------------|:----------|:--------------------------------------|
| `username`   | `string`  | **Required**. Username of customer    |
| `password`   | `string`  | **Required**. Password of customer    |

#### Example: 

* Request : 

```curl
curl -X POST 'localhost:8090/api/auth/login/' \
-H 'Content-Type: application/json' \
-d '{"username":"stevekey","password":"KHG279BD"}'
```

* Response : 

```json

```