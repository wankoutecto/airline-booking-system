package com.example.airline_booking_system.aircraft.dto;

import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import com.example.airline_booking_system.flight.dto.LatestFlightResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AircraftAppendableResponse {
    private Long id;
    private String code;
    private String locationAirport;
    private LatestFlightResponse latestFlight;
}
