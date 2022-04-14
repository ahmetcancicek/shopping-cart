# Shopping Cart
Repository contains basic shopping-cart application to show restful service implementation using Spring Boot and Docker.

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)

## Development

To test the application

```bash
mvn clean test
```

To build and run the application

```bash

```

## Deployment

To deploy this project run

```bash
  docker-compose up -d
```

## API Reference

#### Register customer

```http request
  POST /registration
```

| Parameter    | Type      | Description                        |
|:-------------|:----------|:-----------------------------------|
| `firstName`  | `string`  | **Required**. Name of customer     |
| `lastName`   | `string`  | **Required**. Surname of customer  |
| `email`      | `string`  | **Required**. Email of customer    |
| `username`   | `string`  | **Required**. Username of customer |
| `password`   | `string`  | **Required**. Password of customer |

#### Delete customer

```http request
    DELETE /registration/{customerId}
```

#### Add product to cart 

```http
  POST /cart/add/${productId}
```

| Parameter    | Type   | Description                 |
|:-------------|:-------|:----------------------------|
| `productId`  | `long` | **Required**. Id of product |

#### Remove product from cart

```http
  POST /cart/remove/${productId}
```
| Parameter    | Type   | Description                 |
|:-------------|:-------|:----------------------------|
| `productId`  | `long` | **Required**. Id of product |