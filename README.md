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

| Parameter    | Type      | Description                           |
|:-------------|:----------|:--------------------------------------|
| `firstName`  | `string`  | **Not Required**. Name of customer    |
| `lastName`   | `string`  | **Not Required**. Surname of customer |
| `email`      | `string`  | **Required**. Email of customer       |
| `username`   | `string`  | **Required**. Username of customer    |
| `password`   | `string`  | **Required**. Password of customer    |

```curl
curl -X POST
```

#### Delete customer

```http request
    DELETE /registration/{customerId}
```

| Parameter | Type      | Description                  |
|:----------|:----------|:-----------------------------|
| `id`      | `string`  | **Required**. Id of customer |

```curl
curl -X DELETE 'localhost:8090/registration/{id}'
```


#### Add product

```http request
    POST /products
```

| Parameter     | Type      | Description                              |
|:--------------|:----------|:-----------------------------------------|
| `name`        | `string`  | **Required**. Name of product            |
| `description` | `string`  | **Not Required**. Description of product |
| `price`       | `number`  | **Required**. Price of product           |
| `quantity`    | `number`  | **Required**. Quantity of customer       |

```curl
curl -X POST
```


#### Get product

```http request
    GET /products/{id}
```

| Parameter | Type      | Description                 |
|:----------|:----------|:----------------------------|
| `id`      | `string`  | **Required**. Id of product |

```curl
curl -X GET 'localhost:8090/products/{id}'
```


#### Get products

```http request
    GET /products
```

```curl
curl -X GET 'localhost:8090/products'
```

#### Delete product

```http request
    DELETE /products/{id}
```

| Parameter | Type      | Description                 |
|:----------|:----------|:----------------------------|
| `id`      | `string`  | **Required**. Id of product |

```curl
curl -X DELETE 'localhost:8090/products/{id}'
```


#### Update product

```http request
    PUT /products
```

| Parameter     | Type      | Description                              |
|:--------------|:----------|:-----------------------------------------|
| `id`          | `number`  | **Required**. Name of product            |
| `name`        | `string`  | **Required**. Name of product            |
| `description` | `string`  | **Not Required**. Description of product |
| `price`       | `number`  | **Required**. Price of product           |
| `quantity`    | `number`  | **Required**. Quantity of customer       |


```curl
curl -X PUT
```
