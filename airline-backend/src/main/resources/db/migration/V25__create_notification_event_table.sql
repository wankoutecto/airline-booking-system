create table notification_event(
    id bigint generated always as identity primary key,
    event_id uuid not null unique,
    event_type varchar(250) not null,
    status varchar(50) not null,
    payload jsonb not null,
    created_at timestamp not null
);