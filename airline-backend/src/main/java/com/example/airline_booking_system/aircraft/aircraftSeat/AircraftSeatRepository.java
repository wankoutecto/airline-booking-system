package com.example.airline_booking_system.aircraft.aircraftSeat;

import com.example.airline_booking_system.aircraft.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AircraftSeatRepository extends JpaRepository<AircraftSeat, Long> {
    List<AircraftSeat> findByAircraftId(Long aircraftId);

    boolean existsByAircraftId(Long aircraftId);

    void deleteAllByAircraftId(Long aircraftId);
}
