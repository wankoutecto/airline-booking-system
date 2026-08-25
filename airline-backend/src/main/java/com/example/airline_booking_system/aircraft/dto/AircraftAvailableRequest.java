package com.example.airline_booking_system.aircraft.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class AircraftAvailableRequest {

    @NotBlank(message = "Departure airport is required")
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Airport code must contain exactly 3 uppercase letters"
    )
    private String departureAirport;

    @NotBlank(message = "Arrival airport is required")
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Airport code must contain exactly 3 uppercase letters"
    )
    private String arrivalAirport;

    @NotNull(message = "Departure time is required")
    @FutureOrPresent(message = "Time must be present or in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @FutureOrPresent(message = "Time must be present or in the future")
    private LocalDateTime arrivalTime;
}
