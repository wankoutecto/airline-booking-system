package com.example.airline_booking_system.flight.airport;

import com.example.airline_booking_system.common.entity.BaseEntity;
import com.example.airline_booking_system.flight.enums.AircraftStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "aircraft")
public class Aircraft extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AircraftStatus status;
}
