package com.example.airline_booking_system.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BookingResponse {
    private String flightNumber;
    private String seat;
    private String status;
    private String bookingReference;
    private String seatClass;
    private LocalDateTime createdAt;
}
