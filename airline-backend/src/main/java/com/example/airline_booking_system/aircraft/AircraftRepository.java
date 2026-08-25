package com.example.airline_booking_system.aircraft;

import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    @Query("""
    SELECT a
    FROM Aircraft a
    LEFT JOIN Flight f
        ON f.aircraft.id = a.id
    WHERE f.id IS NULL
    AND a.locationAirport.id = :departureId
    AND a.status = :status
    """)
    List<Aircraft> findAircraftHomeBase(
            @Param("departureId") Long departureId,
            @Param("status") AircraftStatus status
    );
    @Query("""
    SELECT a
    FROM Aircraft a
    JOIN Flight f1
        ON f1.aircraft.id = a.id
    WHERE f1.arrivalTime = (
        SELECT MAX(f2.arrivalTime)
        FROM Flight f2
        WHERE f2.aircraft.id = a.id
    )
    AND f1.arrivalAirport.id = :departureId
    AND f1.arrivalTime <= :departureTime
    AND a.status = :status
    """)
    List<Aircraft> findAircraftAppendable(
            @Param("departureId") Long departureId,
            @Param("departureTime") LocalDateTime departureTime,
            @Param("status") AircraftStatus status
    );
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select a from Aircraft a
            where a.id = :aircraftId
            """)
    Optional<Aircraft> findByIdForUpdate(Long aircraftId);

    boolean existsByCode(String code);
}
