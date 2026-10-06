CREATE TABLE payment(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL,
    order_type VARCHAR(100) NOT NULL,
    amount BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    idempotency_key VARCHAR(50) NOT NULL,
    provider_payment_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT unique_idempotency_key
        UNIQUE(idempotency_key)
);