# Shopping Cart
Repository contains basic shopping-cart application to show restful service implementation using Spring Boot and Docker.

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)

## Development

To test the application

```bash

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
curl -X POST localhost:8090/registration \
-H 'Content-Type: application/json' \
-d '{"firstName":"Bill","lastName":"King","username":"billking","email":"billking@email.com","password":"adj3q8afb"}' 
```

#### Delete customer

```http request
    DELETE /registration/{username}
```

| Parameter    | Type      | Description                           |
|:-------------|:----------|:--------------------------------------|
| `username`   | `string`  | **Required**. Username of customer    |

```curl
curl -X DELETE 'localhost:8090/registration/billking'
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
