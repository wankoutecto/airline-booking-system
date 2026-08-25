package com.example.airline_booking_system.aircraft.aircraftSeat.dto;

import com.example.airline_booking_system.aircraft.enums.SeatClass;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
@Getter
public class SeatConfigurationRequest {

    @NotNull(message = "Seat class is required")
    private SeatClass seatClass;

    @Min(value = 1, message = "Start row must be at least 1")
    private int startRow;

    @Min(value = 1, message = "End row must be at least 1")
    @Max(value =  99, message = "End row must be at most 99")
    private int endRow;

    @NotEmpty(message = "Seat letters are required")
    private List<@Pattern(
            regexp = "^[A-Z]$",
            message = "Seat letter must be uppercase"
    ) String> seatLetters;
}
