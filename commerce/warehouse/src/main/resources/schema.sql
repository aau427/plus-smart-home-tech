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

CREATE TABLE IF NOT EXISTS wh.bookings
(
    booking_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    delivery_id UUID NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_booking_order_id ON wh.bookings(order_id);

CREATE TABLE IF NOT EXISTS wh.booking_products
(
    booking_id UUID NOT NULL REFERENCES wh.bookings(booking_id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES wh.products(product_id) ON DELETE CASCADE,
    quantity   BIGINT,
    CONSTRAINT pk_booking_products PRIMARY KEY (booking_id, product_id)
)
