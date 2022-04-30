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
curl -X POST 'localhost:8090/products/' \
-H 'Content-Type: application/json' \
-d '{"serialNumber":"KAV319","name":"iPhone 14","description":"Apple iPhone 14","price":1000,"quantity":100}'
```

#### Get product

```http request
    GET /products/{serialNumber}
```

| Parameter     | Type      | Description                         |
|:--------------|:----------|:------------------------------------|
| `serialNumber` | `string`  | **Required**. Unique Key of product |

```curl
curl -X GET 'localhost:8090/products/KAV319'

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
    DELETE /products/{serialNumber}
```

| Parameter     | Type      | Description                         |
|:--------------|:----------|:------------------------------------|
| `serialNumber` | `string`  | **Required**. Unique Key of product |

```curl
curl -X DELETE 'localhost:8090/products/KAV319'
```