CREATE TABLE booking(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    booking_reference VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL,
    flight_seat_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_booking_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_booking_flight_seat
        FOREIGN KEY (flight_seat_id)
        REFERENCES flight_seat(id),

    CONSTRAINT unique_booking_reference
        UNIQUE (booking_reference),

    CONSTRAINT unique_booking_flight_seat
        UNIQUE (flight_seat_id)
);