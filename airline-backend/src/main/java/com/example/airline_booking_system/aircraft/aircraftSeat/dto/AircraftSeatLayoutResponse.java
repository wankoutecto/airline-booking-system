package com.example.airline_booking_system.aircraft.aircraftSeat.dto;

import com.example.airline_booking_system.aircraft.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class AircraftSeatLayoutResponse {

    private Long aircraftId;

    private int totalSeats;

    private Map<SeatClass, Long> seatsPerClass;
}
