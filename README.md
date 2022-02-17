# Shopping Cart
Repository contains basic shopping-cart application to show restful service implementation using Spring Boot and Docker.

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)

## Deployment

To deploy this project run

```bash
  docker-compose up -d
```

## API Reference

#### Register customer

```http
  POST /registration
```

| Parameter    | Type      | Description                        |
|:-------------|:----------|:-----------------------------------|
| `firstName`  | `string`  | **Required**. Name of customer     |
| `lastName`   | `string`  | **Required**. Surname of customer  |
| `email`      | `string`  | **Required**. Email of customer    |
| `username`   | `string`  | **Required**. Username of customer |
| `password`   | `string`  | **Required**. Password of customer |


