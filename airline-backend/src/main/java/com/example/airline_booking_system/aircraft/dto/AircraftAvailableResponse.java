package com.example.airline_booking_system.aircraft.dto;

import com.example.airline_booking_system.aircraft.Aircraft;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AircraftAvailableResponse {
    private List<AircraftResponse> aircraftBaseHome;
    private List<AircraftAppendableResponse> aircraftAppendable;
}
