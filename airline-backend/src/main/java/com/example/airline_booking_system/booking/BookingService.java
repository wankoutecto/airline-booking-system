package com.example.airline_booking_system.booking;

import com.example.airline_booking_system.booking.dto.BookingResponse;
import com.example.airline_booking_system.booking.dto.CreateBookingRequest;
import com.example.airline_booking_system.common.exception.BookingProcessingException;
import com.example.airline_booking_system.common.exception.ResourceAlreadyExistsException;
import com.example.airline_booking_system.common.exception.ResourceNotAvailableException;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import com.example.airline_booking_system.common.util.Sha256Util;
import com.example.airline_booking_system.flight.enums.FlightSeatStatus;
import com.example.airline_booking_system.flight.flightSeat.FlightSeat;
import com.example.airline_booking_system.idempotency.Idempotency;
import com.example.airline_booking_system.idempotency.IdempotencyRepository;
import com.example.airline_booking_system.idempotency.IdempotencyStatus;
import com.example.airline_booking_system.user.User;
import com.example.airline_booking_system.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;
    private final Sha256Util sha256Util;
    private final IdempotencyBookingService idempotencyBookingService;



    public BookingResponse processBooking(Long userId,
                                          @Valid CreateBookingRequest request,
                                          String idempotencyKey){

        try {

            return idempotencyBookingService.createBookingWithIdempotency(userId, request, idempotencyKey);

        } catch (DataIntegrityViolationException ex) {

            try {
                Optional<Idempotency> idempotency = idempotencyRepository.findByIdempotencyKey(idempotencyKey);

                String jsonRequest = objectMapper.writeValueAsString(request);
                String requestHash = sha256Util.hash(jsonRequest);

                if (idempotency.isPresent()) {

                    Idempotency existing = idempotency.get();

                    if (!existing.getRequestHash().equals(requestHash)) {
                        throw new ResourceAlreadyExistsException(
                                "Idempotency key was already used with a different request"
                        );
                    }

                    if (existing.getStatus() == IdempotencyStatus.COMPLETED) {
                        return objectMapper.treeToValue(
                                existing.getResponseBody(),
                                BookingResponse.class
                        );
                    }
                }
            } catch (JsonProcessingException jsonEx) {
                throw new BookingProcessingException("Failed to process idempotency response");
            }

            throw  ex;
        }

    }




    public Booking getBooking(Long userId, Long bookingId){
        return bookingRepository.findByUserIdAndId(userId, bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

    }
    public BookingResponse getBookingResponse(Long userId, Long id) {
        return bookingMapper.toBookingResponse(getBooking(userId, id));
    }


    public List<BookingResponse> getAllBookings(Long userId) {
        return bookingMapper.toBookingResponseList(bookingRepository.findAllByUserId(userId));
    }
    @Transactional
    public void cancelBooking(Long userId, Long id) {
        Booking booking = getBooking(userId, id);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getFlightSeat().setStatus(FlightSeatStatus.AVAILABLE);
    }

}
