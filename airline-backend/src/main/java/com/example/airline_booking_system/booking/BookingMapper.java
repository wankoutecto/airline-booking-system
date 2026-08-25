package com.example.airline_booking_system.booking;

import com.example.airline_booking_system.booking.dto.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    public BookingResponse toBookingResponse(Booking booking){
        return BookingResponse.builder()
                .bookingReference(booking.getBookingReference())
                .flightNumber(booking.getFlightSeat().getFlight().getFlightNumber())
                .seatClass(booking.getFlightSeat().getSeatClass().name())
                .seat(booking.getFlightSeat().getSeatNumber())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public List<BookingResponse> toBookingResponseList(List<Booking> booking){
        return booking.stream().map(this::toBookingResponse).toList();
    }

}
