alter table outbox_event
add column aggregate_id BIGINT,
add column aggregate_type VARCHAR(50);