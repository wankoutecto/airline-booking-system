package com.example.airline_booking_system.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class LatestFlightResponse {

    private String flightNumber;

    private String arrivalAirportCode;

    private LocalDateTime arrivalTime;
}
