package com.example.airline_booking_system.booking;

import com.example.airline_booking_system.booking.dto.BookingResponse;
import com.example.airline_booking_system.booking.dto.CreateBookingRequest;
import com.example.airline_booking_system.common.response.ApiResponse;
import com.example.airline_booking_system.flight.dto.FlightAvailableRequest;
import com.example.airline_booking_system.flight.dto.FlightResponse;
import com.example.airline_booking_system.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CreateBookingRequest request){

        BookingResponse response = bookingService.processBooking(user.getId(), request, idempotencyKey);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(response, "Booking successfully created"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id){

        BookingResponse response = bookingService.getBookingResponse(user.getId(), id);

        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings(
            @AuthenticationPrincipal CustomUserDetails user){

        List<BookingResponse> response = bookingService.getAllBookings(user.getId());

        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {

        bookingService.cancelBooking(user.getId(), id);

        return ResponseEntity.noContent().build();
    }


}
