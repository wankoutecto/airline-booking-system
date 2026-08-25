package com.example.airline_booking_system.aircraft.dto;

import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class AircraftRequest {
    @NotBlank(message = "Aircraft code is required")
    private String code;

    @NotBlank(message = "Location airport is required")
    private String locationAirport;
}
