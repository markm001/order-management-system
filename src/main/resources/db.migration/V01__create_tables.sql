CREATE TABLE customer(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_code VARCHAR(80) NOT NULL,
    full_name VARCHAR(80) NOT NULL,
    email VARCHAR(50) NOT NULL,
    telephone VARCHAR(30) NOT NULL
);

CREATE TABLE product(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    sku_code VARCHAR(10) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL
);

CREATE TABLE order(
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   customer_id BIGINT NOT NULL,
   date_of_submission DATETIME(6) NOT NULL,
   FOREIGN KEY(customer_id) REFERENCES customer(id)
);

CREATE TABLE orderLine(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY(order_id) REFERENCES order(id),
    FOREIGN KEY(product_id) REFERENCES product(id)
);