# Shopping Cart

This repository contains a basic sample application to serve restful service with JWT token authentication in a spring
boot application. The application uses maven as a build tool and Docker as a containers

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)

# Technology

1. Spring Boot (2.4.12)
2. Spring Security
3. Spring Data
4. Hibernate
5. Maven
6. JWT (0.11.5)
7. MySQL
8. Java 11
9. Map Struct (1.4.2.Final)
10. Testcontainers (1.16.3)
11. Docker

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

### [User Management Services](#1-user-management-services)

| API                                                     | Description              |
|:--------------------------------------------------------|:-------------------------|
| [`POST/api/auth/register`](#a-register-a-new-user)      | Register a new user      |
| [`POST/api/auth/login`](#b-login-a-user)                | Login a user             |
| [`POST/api/auth/logout`](#c-logout-a-user)              | Logout a user            |
| [`POST/api/auth/refreshtoken`](#d-refrest-token-a-user) | Refresh token for a user |

### [Account Management Services](#2-account-mangement-services)

| API                                                                    | Description                                           |
|:-----------------------------------------------------------------------|:------------------------------------------------------|
| [`GET/api/account/:username`](#a-get-details-of-a-user)                | Get details of a particular customer that had a login |
| [`PATCH/api/account/:username`](#b-update-details-of-a-user)           | Update a particular customer                          |
| [`PATCH/api/account/:username/password`](#c-update-password-of-a-user) | Update password of a particular  customer             |

### [Product Management Services](#3-product-management-services)

| API                                                                                | Description                      |
|:-----------------------------------------------------------------------------------|:---------------------------------|
| [`POST/api/products`](#a-post-a-new-product)                                       | Create a new product             |
| [`GET/api/products`](#b-get-list-of-all-products)                                  | Get list of all products         |
| [`GET/api/products/limit/:limit/page/:page`](#c-get-list-of-all-products-by-limit) | Get list of all products         |
| [`GET/api/products/:serialNumber`](#d-get-details-of-a-product)                    | Get details a particular product |
| [`PATCH/api/products/:serialNumber`](#e-update-details-of-a-product)               | Update a particular product      |
| [`DELETE/api/products/:serialNumber`](#f-delete-a-product)                         | Delete a particular product      |

### [Payment Method Management Services](#4-payment-method-management-services)

| API                                                           | Description                                 |
|:--------------------------------------------------------------|:--------------------------------------------|
| [`GET/api/paymentmethods`](#a-get-list-of-all-paymentmethods) | Get list of all payment methods             |
| [`POST/api/paymentmethods`](#b-post-a-new-paymentmethod)      | Get list of all payments methods            |
| [`DELETE/api/paymentmethods/:id`](#c-delete-a-paymentmethod)  | Get details of a particular payment methods |

### [Shipping Address Management Services](4-shipping-address-management-services)

| API                                                                   | Description                          |
|:----------------------------------------------------------------------|:-------------------------------------|
| [`GET/api/shippingaddress`](#a-get-list-of-all-shippingaddress)       | Get list of all shipping addresses   |
| [`POST/api/shippingaddress`](#b-post-a-new-paymentmethod)             | Create a new shipping address        |
| [`DELETE/api/shippingaddress/:id`](#c-get-list-of-all-paymentmethods) | Delete a particular shipping address |

### [Payment Address Management Services](5-payment-address-management-services)

| API                                                                  | Description                         |
|:---------------------------------------------------------------------|:------------------------------------|
| [`GET/api/paymentaddress`](#a-get-list-of-all-paymentmethods)        | Get list of all payment address     |
| [`POST/api/paymentaddress`](#b-post-a-new-paymentmethod)             | Create a new payment address        |
| [`DELETE/api/paymentaddress/:id`](#c-get-list-of-all-paymentmethods) | Delete a particular payment address |

### [Cart Management Services](#6-cart-management-services)

| API                                                      | Description                     |
|:---------------------------------------------------------|:--------------------------------|
| [`GET/api/cart`](#a-get-list-of-all-items-from-cart)     | Get list of all items from cart |
| [`POST/api/cart`](#b-post-a-new-item-to-cart)            | Add a new item to cart          |
| [`POST/api/cart_bulk`](#c-post-new-items-to-cart)        | Add new items to cart           |
| [`PUT/api/cart`](#d-update-item-from-cart)               | Update item from cart           |
| [`DELETE/api/cart`](#e-delete-item-from-cart)            | Delete item from cart           |
| [`DELETE/api/cart/empty`](#f-delete-all-items-from-cart) | Delete all items from cart      |

### [Order API](#6-order-api)

| API                                                                           | Description                      |
|:------------------------------------------------------------------------------|:---------------------------------|
| [`POST/api/orders`](#b-post-a-new-order)                                      | Create a new order               |
| [`GET/api/orders/limit/:limit/page/:page`](#b-get-list-of-all-order-by-limit) | Get list of all orders           |
| [`GET/api/orders`](#c-get-list-of-all-orders)                                 | Get list of all orders           |
| [`GET/api/orders/:orderNumber`](#d-get-details-of-a-order)                    | Get details of a particular oder |
| [`DELETE/api/orders/:orderNumber`](#e-get-delete-a-order)                     | Delete a particular oder         |

## 1. User Management Services

### a. Register a new user

You can send a POST request to register a new user and returns a web token for authentication.

```
Method: POST
URL: /api/auth/register
Content-Type: application/json
Accept: application/json
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
{
  "status": 200,
  "message": "Authentication is successful!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3MTM3MzYsImlhdCI6MTY1MjY5NTczNiwic3ViIjoic3RldmVrZXkifQ.DztZloT7FfAu1EFKlE4m_wXbSm0QhYmq3rOxCPf3Q5A",
    "username": "stevekey"
  }
}
```

### b. Login a user

You can send a POST request to login a user and returns a web token for authentication.

```
Method: POST
URL: /api/auth/login
Content-Type: application/json
Accept: application/json
```

#### Parameters :

| Parameter    | Type      | Description                           |
|:-------------|:----------|:--------------------------------------|
| `username`   | `string`  | **Required**. Username of customer    |
| `password`   | `string`  | **Required**. Password of customer    |

#### Example :

* Request :

```curl
curl -X POST 'localhost:8090/api/auth/login/' \
-H 'Content-Type: application/json' \
-d '{"username":"georgehouse","password":"DHN827D9N"}'
```

* Response :

```json
{
  "status": 200,
  "message": "Authentication is successful!",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTQwNjIsImlhdCI6MTY1Mjc3NjA2Miwic3ViIjoiZ2VvcmdlaG91c2UifQ.Lgrs-s944JUDl4q9bP3ntijM_WPmlJSOk8inJqQiWDE",
    "username": "georgehouse"
  }
}
```

## 3. Product Management Services

### a. Post a new product

You can send a POST request to create a new product.

```
Method: POST
URL: /api/products
Content-Type: application/json
Accept: application/json
Authorization: Bearer {token}
```

#### Parameters :

| Parameter      | Type     | Description                              |
|:---------------|:---------|:-----------------------------------------|
| `serialNumber` | `string` | **Required**. Serial number of product   |
| `name`         | `string` | **Required**. Name of product            |
| `description`  | `string` | **Not Required**. Description of product |
| `price`        | `number` | **Required**. Price of product           |
| `quantity`     | `number` | **Required**. Quantity of product        |

#### Example :

* Request :

```curl
curl http://localhost:8090/api/products \
   -H "Accept: application/json" \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTA2NzcsImlhdCI6MTY1Mjc3MjY3Nywic3ViIjoiZ2VvcmdlaG91c2UifQ.mbAgLq-o0T4v20qoYW10C1D9PM6LzDOvOIl8nZDwrLk" \
   -d '{"serialNumber":"KAV319","name":"iPhone 14","description":"Apple iPhone 14","price":1000,"quantity":100}' 
```

* Response :

```json
{
  "status": 200,
  "message": "The product has been added successfully.",
  "data": {
    "serialNumber": "KAV319",
    "name": "iPhone 14",
    "description": "Apple iPhone 14",
    "price": 1000,
    "quantity": 100
  }
}
```

### b. Get list of all products

You can send a GET request to get list of all products.

```
Method: GET
URL: /api/products
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X GET http://localhost:8090/api/products \
   -H "Accept: application/json" \
   -H "Content-Type: application/json"
```

* Response :

```json
{
  "status": 200,
  "message": "The product has been got successfully.",
  "data": [
    {
      "serialNumber": "KAV319",
      "name": "iPhone 14",
      "description": "Apple iPhone 14",
      "price": 1000.00,
      "quantity": 100
    },
    {
      "serialNumber": "XH89Ca",
      "name": "iPhone 15",
      "description": "Apple iPhone 15",
      "price": 1500.00,
      "quantity": 20
    }
  ]
}
```

### d. Get details of a product

You can send a GET request to get details of a product.

```
Method: GET
URL: /api/products/{serialNumber}
Accept: application/json
```

#### Example :

* Request :

```curl
curl -X GET http://localhost:8090/api/products/KAV319 \
-H "Accept: application/json" \
-H "Content-Type: application/json" 
```

* Response :

```json
{
  "status": 200,
  "message": "The products has been got successfully.",
  "data": {
    "serialNumber": "KAV319",
    "name": "iPhone 14",
    "description": "Apple iPhone 14",
    "price": 1000.00,
    "quantity": 100
  }
}
```

### e. Update a particular product

You can send a PUT request to update a particular product.

```
Method: PUT
URL: /api/products
Content: application/json
Accept: application/json
Authorization: Bearer {token}
```

#### Parameters :

| Parameter      | Type     | Description                              |
|:---------------|:---------|:-----------------------------------------|
| `serialNumber` | `string` | **Required**. Serial number of product   |
| `name`         | `string` | **Required**. Name of product            |
| `description`  | `string` | **Not Required**. Description of product |
| `price`        | `number` | **Required**. Price of product           |
| `quantity`     | `number` | **Required**. Quantity of product        |

#### Example :

* Request :

```curl
curl -X PUT http://localhost:8090/api/products \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTA2NzcsImlhdCI6MTY1Mjc3MjY3Nywic3ViIjoiZ2VvcmdlaG91c2UifQ.mbAgLq-o0T4v20qoYW10C1D9PM6LzDOvOIl8nZDwrLk" \
-d '{"serialNumber":"KAV319","name":"iPhone 14","description":"Apple iPhone 14","price":1200,"quantity":100}' 
```

* Response :

```json
{
  "status": 200,
  "message": "The product has been updated successfully.",
  "data": {
    "serialNumber": "KAV319",
    "name": "iPhone 14",
    "description": "Apple iPhone 14",
    "price": 1200,
    "quantity": 100
  }
}
```

### f. Delete a particular product

You can send a DELETE request to delete a particular product.

```
Method: DELETE
URL: /api/products/{serialNumber}
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X DELETE http://localhost:8090/api/products/KAV319 \
-H "Accept: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTA2NzcsImlhdCI6MTY1Mjc3MjY3Nywic3ViIjoiZ2VvcmdlaG91c2UifQ.mbAgLq-o0T4v20qoYW10C1D9PM6LzDOvOIl8nZDwrLk"
```

* Response :

```json
{
  "status": 200,
  "message": "The product has been deleted successfully.",
  "data": null
}
```
