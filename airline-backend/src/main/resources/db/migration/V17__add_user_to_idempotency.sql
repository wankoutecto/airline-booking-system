alter table idempotency
add column user_id bigint;

alter table idempotency
add constraint fk_idempotency_user
foreign key (user_id)
references users(id);

alter table idempotency
add constraint unique_idempotency_user
unique (idempotency_key, user_id);