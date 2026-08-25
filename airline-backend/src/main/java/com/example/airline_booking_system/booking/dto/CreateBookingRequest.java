package com.example.airline_booking_system.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBookingRequest {
    @NotNull(message = "Flight id is required")
    @Positive(message = "Flight id must be greater than zero")
    private Long flightId;

    @NotBlank
    @Pattern(
            regexp = "^([1-9]|[12][0-9]|30)[A-L]$",
            message = "Seat number goes from 1 to 30 rows and seat letter A-L. ex: 1A, 21H or 30L"
    )
    private String seatNumber;
}
