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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

// Created this separate service so the transactional booking operation
// (createBookingWithIdempotency) is called through Spring's proxy,
// allowing @Transactional to be applied correctly.
@Service
@RequiredArgsConstructor
public class IdempotencyBookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final IdempotencyRepository idempotencyRepository;
    private final ObjectMapper objectMapper;
    private final Sha256Util sha256Util;

    @Value("${app.idempotency-expiration}")
    private Duration idempotencyExpiration;

    @Transactional
    public BookingResponse createBookingWithIdempotency(Long userId,
                                                        @Valid CreateBookingRequest request,
                                                        String idempotencyKey){


        try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            String requestHash = sha256Util.hash(jsonRequest);

            Idempotency idempotency = Idempotency.builder()
                    .idempotencyKey(idempotencyKey)
                    .status(IdempotencyStatus.PROCESSING)
                    .requestHash(requestHash)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plus(idempotencyExpiration))
                    .build();

            //where the race happens
            idempotencyRepository.save(idempotency);

            //start booking creation
            if(bookingRepository.existsByUserIdAndFlightSeatFlightId(userId, request.getFlightId())){
                throw new ResourceAlreadyExistsException("User already has a booking for this flight");
            }

            FlightSeat flightSeat = bookingRepository
                    .findByFlightIdAndSeatNumberForUpdate(request.getFlightId(), request.getSeatNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Flight's Seat not found"));

            if(flightSeat.getStatus() != FlightSeatStatus.AVAILABLE){
                throw new ResourceNotAvailableException("Seat is already booked");
            }

            User user = userService.findUserById(userId);

            String reference;

            do {
                reference = generateBookingReference();
            } while (bookingRepository.existsByBookingReference(reference));

            Booking booking = Booking.builder()
                    .bookingReference(reference)
                    .user(user)
                    .status(BookingStatus.CONFIRMED)
                    .flightSeat(flightSeat)
                    .build();

            Booking savedBooking = bookingRepository.save(booking);
            flightSeat.setStatus(FlightSeatStatus.BOOKED);
            //end booking

            BookingResponse response = bookingMapper.toBookingResponse(savedBooking);

            idempotency.setStatus(IdempotencyStatus.COMPLETED);
            idempotency.setResponseStatus(201);
            idempotency.setResponseBody(objectMapper.valueToTree(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new BookingProcessingException("Failed to process idempotency response");
        }
    }

    public String generateBookingReference(){

        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
