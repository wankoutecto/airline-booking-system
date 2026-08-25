package com.example.airline_booking_system.airport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AirportRequest {
    @NotBlank(message = "Departure airport is required")
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Airport code must contain exactly 3 uppercase letters"
    )
    private String code;
    private String name;
    private String city;
    private String country;
}
