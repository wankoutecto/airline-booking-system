CREATE TABLE flight (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    flight_number VARCHAR(50) NOT NULL UNIQUE,

    aircraft_id BIGINT NOT NULL,

    departure_airport_id BIGINT NOT NULL,

    arrival_airport_id BIGINT NOT NULL,

    departure_time TIMESTAMP NOT NULL,

    arrival_time TIMESTAMP NOT NULL,

    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_flight_aircraft
        FOREIGN KEY (aircraft_id)
        REFERENCES aircraft(id),

    CONSTRAINT fk_flight_departure_airport
        FOREIGN KEY (departure_airport_id)
        REFERENCES airport(id),

    CONSTRAINT fk_flight_arrival_airport
        FOREIGN KEY (arrival_airport_id)
        REFERENCES airport(id),

    CONSTRAINT chk_different_airports
        CHECK (departure_airport_id <> arrival_airport_id)
);