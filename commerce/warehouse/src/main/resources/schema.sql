CREATE SCHEMA IF NOT EXISTS wh;

CREATE TABLE IF NOT EXISTS wh.products
(
    product_id UUID PRIMARY KEY,
    fragile    BOOLEAN NOT NULL,
    width      DECIMAL(8, 2) NOT NULL CHECK (width >= 1),
    height     DECIMAL(8, 2) NOT NULL CHECK (height >= 1),
    depth      DECIMAL(8, 2) NOT NULL CHECK (depth >= 1),
    weight     DECIMAL(8, 2) NOT NULL CHECK (weight >= 1),
    quantity   BIGINT NOT NULL CHECK (quantity >= 0)
);