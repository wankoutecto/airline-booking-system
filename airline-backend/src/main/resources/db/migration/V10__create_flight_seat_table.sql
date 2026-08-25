CREATE TABLE flight_seat(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    flight_id BIGINT NOT NULL,

    seat_number VARCHAR(10) NOT NULL,

    seat_class VARCHAR(20) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_flight_seat_flight
        FOREIGN KEY (flight_id)
        REFERENCES flight(id),

    CONSTRAINT unique_flight_seat
        UNIQUE (flight_id, seat_number)
);