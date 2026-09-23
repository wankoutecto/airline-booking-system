create table processed_event(
    id bigint generated always as identity primary key,
    event_id uuid not null unique,
    processed_at timestamp not null
);