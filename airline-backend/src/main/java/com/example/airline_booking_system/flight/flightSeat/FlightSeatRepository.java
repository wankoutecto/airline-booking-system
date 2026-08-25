package com.example.airline_booking_system.flight.flightSeat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {
    List<FlightSeat> findAllByFlightId(Long flightId);
}
