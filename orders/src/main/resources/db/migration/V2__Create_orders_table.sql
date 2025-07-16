CREATE TABLE IF NOT EXISTS orders
(
    order_id         BIGSERIAL PRIMARY KEY,
    order_number     BIGINT         NOT NULL UNIQUE,
    order_date       TIMESTAMP DEFAULT now(),
    shipping_address VARCHAR        NOT NULL,
    total_price      DECIMAL(10, 2) NOT NULL,
    order_status     varchar   DEFAULT 'NEW',
    user_id          BIGINT         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);


