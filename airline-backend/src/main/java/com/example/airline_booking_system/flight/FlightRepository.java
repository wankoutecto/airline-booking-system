package com.example.airline_booking_system.flight;

import com.example.airline_booking_system.aircraft.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByAircraft(Aircraft aircraft);
    @Query(value = """
    SELECT *
    FROM flight
    WHERE aircraft_id = :aircraftId
    ORDER BY arrival_time DESC
    LIMIT 1
    """, nativeQuery = true)
    Optional<Flight> findAircraftLatestFlight(@Param("aircraftId") Long aircraftId);

    List<Flight> findAllByAircraftId(Long aircraftId);

    @Query(value = """
            select f
            from Flight f
            where f.aircraft.id IN :aircraftIds
            and f.arrivalTime = (
                select Max(f2.arrivalTime)
                from Flight f2
                where f2.aircraft.id = f.aircraft.id
            )
            """)
    List<Flight> findLatestFlightByAircraftIds(@Param("aircraftIds") List<Long> aircraftIds);

    @Query("""
        SELECT f
        FROM Flight f
        WHERE f.departureAirport.code = :departure
        AND f.arrivalAirport.code = :arrival
        AND f.departureTime >= :departureTime
        AND f.status = 'SCHEDULED'
    """)
    List<Flight> findBookableFlights(
            @Param("departure") String departure,
            @Param("arrival") String arrival,
            @Param("departureTime") LocalDateTime departureTime
    );
}
