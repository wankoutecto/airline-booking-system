CREATE TABLE provider_webhook_event(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    provider_payment_id VARCHAR(255),
    provider_refund_id VARCHAR(255),
    payment_id BIGINT NOT NULL,
    refund_id BIGINT,
    received_at TIMESTAMP NOT NULL,

    CONSTRAINT unique_event
        UNIQUE(event_id),

    CONSTRAINT fk_webhook_payment
        FOREIGN KEY (payment_id) REFERENCES payment(id),

    CONSTRAINT fk_webhook_refund
        FOREIGN KEY (refund_id) REFERENCES refund(id)
);