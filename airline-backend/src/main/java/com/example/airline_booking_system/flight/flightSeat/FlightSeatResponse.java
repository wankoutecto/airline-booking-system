package com.example.airline_booking_system.flight.flightSeat;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightSeatResponse {
    private String seat;
    private String seatClass;
    private String status;

}
