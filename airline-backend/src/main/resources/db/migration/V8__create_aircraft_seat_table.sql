CREATE TABLE aircraft_seat(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    aircraft_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_class VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_aircraft_seat_aircraft
        FOREIGN KEY (aircraft_id)
        REFERENCES aircraft(id),

    CONSTRAINT unique_aircraft_seat
        UNIQUE (aircraft_id, seat_number)
);