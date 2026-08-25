package com.example.airline_booking_system.aircraft.aircraftSeat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
@Getter
public class GenerateAircraftSeatsRequest {
    @NotEmpty
    private List<SeatConfigurationRequest> seatConfigurations;
}
