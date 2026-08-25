package com.example.airline_booking_system.flight.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateFlightRequest {
    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    @NotBlank(message = "Flight number is required")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{3}$",
            message = "Flight number must be 2 uppercase letters followed by at least 3 digits"
    )
    private String flightNumber;

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

    @FutureOrPresent(message = "Departure time must be today or in the future")
    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @FutureOrPresent(message = "Arrival time must be today or in the future")
    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;
}
