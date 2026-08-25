package com.example.airline_booking_system.booking;

import com.example.airline_booking_system.flight.Flight;
import com.example.airline_booking_system.flight.flightSeat.FlightSeat;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            select fs
            from FlightSeat fs
            where fs.flight.id = :flightId
            and fs.seatNumber = :seatNumber
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FlightSeat> findByFlightIdAndSeatNumberForUpdate(@Param("flightId") Long flightId,
                                                              @Param("seatNumber") String seatNumber);

    boolean existsByBookingReference(String reference);

    boolean existsByUserIdAndFlightSeatFlightId(Long userId, Long flightId);


    Optional<Booking> findByUserIdAndId(Long userId, Long bookingId);

    List<Booking> findAllByUserId(Long userId);
}
