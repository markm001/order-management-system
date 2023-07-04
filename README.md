# Order Management System API
Allows creation of Customers, Products and Orders.
Orders can be searched by date-range, Product or Customer.
The quantity in each Order Line can be updated with the Id and new quantity.
Liquibase has been utilized to allow Database-Migration.


## Setup

------------------------------------------------------------------------------------------

Clone the repository and open the project in your IDE of choice:
```
git clone https://github.com/markm001/order-management-system.git
```

Before running configure the project with datasource and relevant dialect:
```lombok.config
server.servlet.context-path = /api

spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect
spring.datasource.url=jdbc:h2:mem:ordersystem-dev
spring.datasource.username = OrderSystem
spring.datasource.password = 1234
#spring.datasource.driverClassName=

spring.jpa.hibernate.ddl-auto=none

spring.liquibase.enabled=true
```

## API Reference

------------------------------------------------------------------------------------------
###### A Postman-Collection has been included to allow simple access to these Endpoints:
#### Create Customer
<details>
 <summary><code>POST</code> <code><b>/api/customer</b></code> <code>(creates new Customer)</code></summary>

##### cURL:

```http request
    curl 'localhost:8080/api/customer' \
    -H 'Content-Type: application/json' \
    --data-raw '{
            "registrationCode": "CODE",
            "fullName": "Max Muster",
            "email": "Max@Muster.de",
            "telephone": "0123456789"
    }'
```

</details>


#### Create Product
<details>
 <summary><code>POST</code> <code><b>/api/product</b></code> <code>(creates new Product)</code></summary>

##### cURL:

```http request
    curl 'localhost:8080/api/product' \
    -H 'Content-Type: application/json' \
    -d '{
        "name": "Test Product",
        "skuCode": "TPC-123",
        "unitPrice": 123
    }'
```

</details>


#### Create Order
<details>
 <summary><code>POST</code> <code><b>/api/order/${date}</b></code> <code>(creates new Order)</code></summary>

##### cURL:

```http request
    curl 'localhost:8080/api/order' \
    -H 'Content-Type: application/json' \
    -d '{
        "orderLineList": [
            {
                "productId": 1,
                "quantity": 12
            },
            {
                "productId": 2,
                "quantity": 24
            }
        ],
        "customerId": 1
    }'
```

| Parameter | Type        | Description                                          |
|:----------|:------------|:-----------------------------------------------------|
| `date`    | `LocalDate` | **Optional**. For testing purposes date can be added |

</details>


#### Search Order By Dates
<details>
 <summary><code>GET</code> <code><b>/api/order</b></code> <code>(finds Orders between the specified dates)</code></summary>

##### cURL:

```http request
    curl 'localhost:8080/api/product' \
    -H 'Content-Type: application/json' \
    -d '{
        "lowerBound": "2023-06-03",
        "upperBound": "2023-07-08"
    }'
```

</details>

#### Search Order For Customer or Product ID
<details>
 <summary><code>GET</code> <code><b>/api/order?customer=${customerId}</b></code> <code>(finds Orders for a customer)</code></summary>
 <summary><code>GET</code> <code><b>/api/order?product=${productId}</b></code> <code>(finds Orders that contain a product)</code></summary>

##### cURL:

```http request
    curl 'localhost:8080/api/order?product=1'
    curl 'localhost:8080/api/order?customer=1'
```

| Parameter    | Type   | Description                      |
|:-------------|:-------|:---------------------------------|
| `productId`  | `long` | **Optional**. Id of the Product  |
| `customerId` | `long` | **Optional**. Id of the Customer |

</details>


#### Update quantity of products in an Order Line
<details>
 <summary><code>PUT</code> <code><b>/api/order/orderline/{orderLineId}?quantity=${quantity}</b></code> <code>(updates a specified Order Line with a quantity)</code></summary>

##### cURL:

```http request
    curl -X PUT 'localhost:8080/api/order/orderline/13412851610773941222?quantity=99'
```

| Parameter    | Type   | Description                                 |
|:-------------|:-------|:--------------------------------------------|
| `oderLineId` | `long` | **Required**. Id of the OrderLine to modify |
| `quantity`   | `int`  | **Required**. Quantity to set               |

</details>


## Encountered Issues

------------------------------------------------------------------------------------------

- Table names like ORDER require globally_quoted_identifiers to be true, native queries have to include " ":
  - names were altered to include '-s' -> order(s)

- Updating OrderLine quantity: Embeddable Table cannot be directly queried with JPQL,
two solutions can be applied:
  1. Convert Embeddable to an Entity (better)
  2. Use Native SQL query to update the table.
