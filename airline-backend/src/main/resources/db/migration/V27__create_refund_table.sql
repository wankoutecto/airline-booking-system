CREATE TABLE refund(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL,
    order_type VARCHAR(100) NOT NULL,
    amount BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    idempotency_key VARCHAR(50) NOT NULL,
    provider_payment_id VARCHAR(255),
    provider_refund_id VARCHAR(255),
    payment_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uq_refund_idempotency_key
        UNIQUE(idempotency_key),

    CONSTRAINT fk_refund_payment
        FOREIGN KEY (payment_id) REFERENCES payment(id)
);
