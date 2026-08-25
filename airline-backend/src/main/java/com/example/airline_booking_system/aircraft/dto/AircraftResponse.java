package com.example.airline_booking_system.aircraft.dto;

import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import com.example.airline_booking_system.flight.dto.LatestFlightResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "locationAirport", "code", "status"})
public class AircraftResponse {
    private Long id;
    private String code;
    private String locationAirport;
    private String status;
}
