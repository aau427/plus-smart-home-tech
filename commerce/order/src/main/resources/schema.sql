CREATE TABLE IF NOT EXISTS orders
(
    order_id UUID PRIMARY KEY,
    username varchar(50) NOT NULL,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR(50) NOT NULL,
    delivery_weight DECIMAL(10, 2),
    delivery_volume DECIMAL(10, 2),
    fragile BOOLEAN,
    total_price DECIMAL(10, 2),
    delivery_price   DECIMAL(10, 2),
    product_price    DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS order_products
(
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_orders_username ON orders(username);