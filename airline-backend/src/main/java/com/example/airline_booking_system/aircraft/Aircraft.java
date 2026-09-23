package com.example.airline_booking_system.aircraft;

import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import com.example.airline_booking_system.airport.Airport;
import com.example.airline_booking_system.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "aircraft")
public class Aircraft extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    @Enumerated(EnumType.STRING)
    private AircraftStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_airport_id")
    private Airport locationAirport;

    @Version
    private Long version;
}
