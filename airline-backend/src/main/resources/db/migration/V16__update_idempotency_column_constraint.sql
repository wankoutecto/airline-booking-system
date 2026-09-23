ALTER TABLE idempotency
ALTER COLUMN response_body DROP NOT NULL,
ALTER COLUMN response_status DROP NOT NULL;
