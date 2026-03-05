CREATE TABLE IF NOT EXISTS payments
(
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NUll,
    delivery_total NUMERIC(19,2),
    total_payment NUMERIC(19,2),
    fee_total NUMERIC(19,2),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);