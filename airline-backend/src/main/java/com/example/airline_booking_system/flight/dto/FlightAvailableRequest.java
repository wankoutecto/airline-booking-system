package com.example.airline_booking_system.flight.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class FlightAvailableRequest {
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

    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    @NotNull(message = "Arrival date is required")
    private LocalDate arrivalDate;
}
