# Shopping Cart

This repository contains a basic sample application to serve restful service with JWT token authentication in a spring
boot application. The application uses maven as a build tool and Docker as a containers

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)

# Technology

1. Spring Boot (2.4.12)
2. Spring Security
3. Spring Data
4. Hibernate
5. Maven (3.8.6)
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
docker-compose -f docker-compose.dev.yml up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

To update the maven wapper

```bash
mvn wrapper:wrapper
```

## Deployment

To deploy this project run

```bash
./mvnw clean install -Dmaven.test.skip=true
docker-compose up -d
```

## API Documentation

To review the API with Swagger

```
http://localhost:8090/swagger-ui/#/
```

## API Routes

### [User Management Services](#1-user-management-services)

| API                                                        | Description              |
|:-----------------------------------------------------------|:-------------------------|
| [`POST/api/v1/auth/register`](#a-register-a-new-user)      | Register a new user      |
| [`POST/api/v1/auth/login`](#b-login-a-user)                | Login a user             |
| [`POST/api/v1/auth/logout`](#c-logout-a-user)              | Logout a user            |
| [`POST/api/v1/auth/refreshtoken`](#d-refrest-token-a-user) | Refresh token for a user |

### [Account Management Services](#2-account-mangement-services)

| API                                                                       | Description                                           |
|:--------------------------------------------------------------------------|:------------------------------------------------------|
| [`GET/api/v1/account/:username`](#a-get-details-of-a-user)                | Get details of a particular customer that had a login |
| [`PATCH/api/v1/account/:username`](#b-update-details-of-a-user)           | Update a particular customer                          |
| [`PATCH/api/v1/account/:username/password`](#c-update-password-of-a-user) | Update password of a particular  customer             |

### [Product Management Services](#3-product-management-services)

| API                                                                                   | Description                      |
|:--------------------------------------------------------------------------------------|:---------------------------------|
| [`POST/api/v1/products`](#a-create-a-new-product)                                     | Create a new product             |
| [`GET/api/v1/products`](#b-get-list-of-all-products)                                  | Get list of all products         |
| [`GET/api/v1/products/limit/:limit/page/:page`](#c-get-list-of-all-products-by-limit) | Get list of all products         |
| [`GET/api/v1/products/:serialNumber`](#d-get-details-of-a-particular-product)         | Get details a particular product |
| [`PATCH/api/v1/products/:serialNumber`](#e-update-details-of-a-particular-product)    | Update a particular product      |
| [`DELETE/api/v1/products/:serialNumber`](#f-delete-a-particular-product)              | Delete a particular product      |

### [Payment Method Management Services](#4-payment-method-management-services)

| API                                                                              | Description                                 |
|:---------------------------------------------------------------------------------|:--------------------------------------------|
| [`GET/api/v1/paymentmethods/:id`](#a-get-details-of-a-particular-payment-method) | Get details of a particular payment methods |
| [`POST/api/v1/paymentmethods`](#b-create-a-new-payment-method)                   | Create a new payment method                 |
| [`DELETE/api/v1/paymentmethods/:id`](#c-delete-a-particular-payment-method)      | Delete a particular payment method          |

### [Address Management Services](4-address-management-services)

| API                                                                    | Description                           |
|:-----------------------------------------------------------------------|:--------------------------------------|
| [`GET/api/v1/addresses/:id`](#a-get-details-of-a-particular-addresses) | Get details of a particular addresses |
| [`POST/api/v1/addresses`](#b-create-a-new-address)                     | Create a new address                  |
| [`DELETE/api/v1/addresses/:id`](#c-delete-a-particular-address)        | Delete a particular address           |

### [Cart Management Services](#6-cart-management-services)

| API                                                            | Description                     |
|:---------------------------------------------------------------|:--------------------------------|
| [`GET/api/v1/cart`](#a-get-list-of-all-items-from-cart)        | Get list of all items from cart |
| [`POST/api/v1/cart`](#b-post-a-new-item-to-cart)               | Add a new item to cart          |
| [`POST/api/v1/cart_bulk`](#c-post-new-items-to-cart)           | Add new items to cart           |
| [`PUT/api/v1/cart`](#d-update-item-from-cart)                  | Update item from cart           |
| [`DELETE/api/v1/cart/:serialNumber`](#e-delete-item-from-cart) | Delete item from cart           |
| [`DELETE/api/v1/cart/empty`](#f-delete-all-items-from-cart)    | Delete all items from cart      |

### [Order API](#6-order-api)

| API                                                                              | Description                      |
|:---------------------------------------------------------------------------------|:---------------------------------|
| [`POST/api/v1/orders`](#b-post-a-new-order)                                      | Create a new order               |
| [`GET/api/v1/orders/limit/:limit/page/:page`](#b-get-list-of-all-order-by-limit) | Get list of all orders           |
| [`GET/api/v1/orders`](#c-get-list-of-all-orders)                                 | Get list of all orders           |
| [`GET/api/v1/orders/:orderNumber`](#d-get-details-of-a-order)                    | Get details of a particular oder |
| [`DELETE/v1/api/orders/:orderNumber`](#e-get-delete-a-order)                     | Delete a particular oder         |

## 1. User Management Services

### a. Register a new user

You can send a POST request to register a new user and returns a web token for authentication.

```
Method: POST
URL: /api/v1/auth/register
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
curl -X POST 'localhost:8090/api/v1/auth/register/' \
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
URL: /api/v1/auth/login
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
curl -X POST 'localhost:8090/api/v1/auth/login/' \
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

### a. Create a new product

You can send a POST request to create a new product.

```
Method: POST
URL: /api/v1/products
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
curl http://localhost:8090/api/v1/products \
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
URL: /api/v1/products
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X GET http://localhost:8090/api/v1/products \
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

### d. Get details of a particular product

You can send a GET request to get details of a product.

```
Method: GET
URL: /api/v1/products/{serialNumber}
Accept: application/json
```

#### Example :

* Request :

```curl
curl -X GET http://localhost:8090/api/v1/products/KAV319 \
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

### e. Update details of a particular product

You can send a PUT request to update a particular product.

```
Method: PUT
URL: /api/v1/products
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
curl -X PUT http://localhost:8090/api/v1/products \
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
URL: /api/v1/products/{serialNumber}
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X DELETE http://localhost:8090/api/v1/products/KAV319 \
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

## 4. Payment Method Management Services

### a. Get details of a particular payment method

You can send a GET request to get details of a particular payment methods.

```
Method: GET
URL: /api/v1/paymentmethod/{id}
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X GET http://localhost:8090/api/v1/paymentmethod/14 \
-H "Accept: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTA2NzcsImlhdCI6MTY1Mjc3MjY3Nywic3ViIjoiZ2VvcmdlaG91c2UifQ.mbAgLq-o0T4v20qoYW10C1D9PM6LzDOvOIl8nZDwrLk"
```

* Response :

```json
{
  "status": 200,
  "message": "The payment method has been got successfully",
  "data": {
    "id": 14,
    "paymentType": "VISA",
    "name": "My VISA"
  }
}
```

### b. Create a new payment method

You can send a POST request to create a new payment method.

```
Method: POST
URL: /api/v1/paymentmethod
Content-Type: application/json
Accept: application/json
Authorization: Bearer {token}
```

#### Parameters:

| Parameter     | Type     | Description                          |
|:--------------|:---------|:-------------------------------------|
| `name`        | `string` | **Required**. Name of payment method |
| `paymentType` | `string` | **Required**. Type of payment method |

#### Example :

* Request :

```curl
curl -X POST http://localhost:8090/api/v1/paymentmethod \
-H "Accept: application/json" \
-H "Content: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTA2NzcsImlhdCI6MTY1Mjc3MjY3Nywic3ViIjoiZ2VvcmdlaG91c2UifQ.mbAgLq-o0T4v20qoYW10C1D9PM6LzDOvOIl8nZDwrLk" \
-d '{"name":"MY VISA","paymentType":"VISA"}'
```

* Response :

```json
{
  "status": 201,
  "message": "The payment method has added successfully",
  "data": {
    "id": 14,
    "paymentType": "VISA",
    "name": "My VISA"
  }
}
```

### c. Delete a particular payment method

You can send a DELETE request to delete a particular payment method.

```
Method: GET
URL: /api/v1/paymentmethod/{id}
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X DELETE http://localhost:8090/api/v1/paymentmethod/13 \
-H "Accept: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTI3OTA2NzcsImlhdCI6MTY1Mjc3MjY3Nywic3ViIjoiZ2VvcmdlaG91c2UifQ.mbAgLq-o0T4v20qoYW10C1D9PM6LzDOvOIl8nZDwrLk" 
```

* Response :

```json
{
  "status": 200,
  "message": "The payment method has been deleted successfully",
  "data": null
}
```

## 7. Cart Management Services

### a. Get list of all items from cart

You can send a GET request to get list of all items from cart.

```
Method: GET
URL: /api/v1/cart
Accept: application/json
Authorization: Bearer {token}
```

#### Example :

* Request :

```curl
curl -X GET http://localhost:8090/api/v1/cart \
-H "Accept: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTM5MjEyNTEsImlhdCI6MTY1MzkwMzI1MSwic3ViIjoiZ2VvcmdlaG91c2UifQ.VvKRulyV9TYVrTIb5XxAjmsaYNMUxlhLySHfb9GCOWY"
```

* Response :

```json
{
  "data": {
    "items": [
      {
        "description": "Apple iPhone 14",
        "name": "iPhone 14",
        "price": 1000,
        "quantity": 2,
        "serialNumber": "KAV319"
      },
      {
        "description": "Apple iPhone 15",
        "name": "iPhone 15",
        "price": 1200,
        "quantity": 1,
        "serialNumber": "HA68VAN"
      }
    ],
    "totalPrice": 3200,
    "totalQuantity": 3,
    "username": "georgehouse"
  },
  "message": "The item has been added successfully",
  "status": 200
}
```

### b. Add new item to cart

You can send a POST request to add item to cart.

```
Method: POST
URL: /api/v1/cart
Accept: application/json
Content: application/json
Authorization: Bearer {token}
```

#### Parameters :

| Parameter      | Type     | Description                            |
|:---------------|:---------|:---------------------------------------|
| `serialNumber` | `string` | **Required**. Serial number of product |
| `quantity`     | `number` | **Required**. Quantity of product      |

#### Example :

* Request :

```curl
curl -X POST http://localhost:8090/api/v1/cart \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTM5MjEyNTEsImlhdCI6MTY1MzkwMzI1MSwic3ViIjoiZ2VvcmdlaG91c2UifQ.VvKRulyV9TYVrTIb5XxAjmsaYNMUxlhLySHfb9GCOWY" \
-d '{"serialNumber":"KAV319","quantity":2}' 
```

* Response :

```json
{
  "data": {
    "items": [
      {
        "description": "Apple iPhone 14",
        "name": "iPhone 14",
        "price": 1000,
        "quantity": 2,
        "serialNumber": "KAV319"
      },
      {
        "description": "Apple iPhone 15",
        "name": "iPhone 15",
        "price": 1200,
        "quantity": 1,
        "serialNumber": "HA68VAN"
      }
    ],
    "totalPrice": 3200,
    "totalQuantity": 3,
    "username": "georgehouse"
  },
  "message": "The item has been added successfully",
  "status": 200
}
```

### c. Add new items to cart

#### Example :

* Request :

* Response :

### d. Update item from cart

You can send a PUT request to update item from cart.

#### Example :

* Request :

```curl
curl -X PUT http://localhost:8090/api/v1/cart \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTM5MjEyNTEsImlhdCI6MTY1MzkwMzI1MSwic3ViIjoiZ2VvcmdlaG91c2UifQ.VvKRulyV9TYVrTIb5XxAjmsaYNMUxlhLySHfb9GCOWY" \
-d '{"serialNumber":"KAV319","quantity":5}' 
```

* Response :

```json
{
  "data": {
    "items": [
      {
        "description": "Apple iPhone 14",
        "name": "iPhone 14",
        "price": 1000,
        "quantity": 5,
        "serialNumber": "KAV319"
      },
      {
        "description": "Apple iPhone 15",
        "name": "iPhone 15",
        "price": 1200,
        "quantity": 1,
        "serialNumber": "HA68VAN"
      }
    ],
    "totalPrice": 6200,
    "totalQuantity": 6,
    "username": "georgehouse"
  },
  "message": "The items has been updated successfully",
  "status": 200
}
```

### e. Delete item from cart

You can send a DELETE request to delete item to cart.

#### Example :

```
Method: DELETE
URL: /api/v1/cart/{serialNumber}
Content: application/json
Authorization: Bearer {token}
```

* Request :

```curl
curl -X DELETE http://localhost:8090/api/v1/cart/KAV319 \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTM5MjEyNTEsImlhdCI6MTY1MzkwMzI1MSwic3ViIjoiZ2VvcmdlaG91c2UifQ.VvKRulyV9TYVrTIb5XxAjmsaYNMUxlhLySHfb9GCOWY" 
```

* Response :

```json
{
  "data": {
    "items": [
      {
        "description": "Apple iPhone 15",
        "name": "iPhone 15",
        "price": 1200,
        "quantity": 1,
        "serialNumber": "HA68VAN"
      }
    ],
    "totalPrice": 1200,
    "totalQuantity": 1,
    "username": "georgehouse"
  },
  "message": "The item has been deleted successfully",
  "status": 200
}
```

### f. Delete all items from cart

You can send a DELETE request to delete all items to cart.

#### Example :

```
Method: DELETE
URL: /api/v1/cart/empty
Accept: application/json
Content: application/json
Authorization: Bearer {token}
```

* Request :

```curl
curl -X DELETE http://localhost:8090/api/v1/cart/empty \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTM5MjEyNTEsImlhdCI6MTY1MzkwMzI1MSwic3ViIjoiZ2VvcmdlaG91c2UifQ.VvKRulyV9TYVrTIb5XxAjmsaYNMUxlhLySHfb9GCOWY"
```

* Response :

```json
{
  "data": {
    "items": [],
    "totalPrice": 0,
    "totalQuantity": 0,
    "username": "georgehouse"
  },
  "message": "The items has been deleted successfully",
  "status": 200
}
```