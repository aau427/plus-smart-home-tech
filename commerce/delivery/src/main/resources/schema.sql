CREATE TABLE IF NOT EXISTS deliveries
(
    delivery_id UUID PRIMARY KEY,
    order_id UUID NOT NUll,
    delivery_state VARCHAR(100) NOT NUll,
    country_from VARCHAR(100),
    city_from VARCHAR(100),
    street_from VARCHAR(100),
    house_from VARCHAR(100),
    flat_from VARCHAR(100),

    country_to VARCHAR(100),
    city_to VARCHAR(100),
    street_to VARCHAR(100),
    house_to VARCHAR(100),
    flat_to VARCHAR(100)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_deliveries_order_id ON deliveries(order_id);