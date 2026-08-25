package com.example.airline_booking_system.flight.dto;

import com.example.airline_booking_system.flight.enums.FlightStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FlightResponse {

    private Long id;

    private String flightNumber;

    private String aircraftCode;

    private String departureAirportCode;

    private String arrivalAirportCode;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private FlightStatus status;
}
