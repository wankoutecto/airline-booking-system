package com.example.airline_booking_system.flight.airport;

import com.example.airline_booking_system.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "airport")
public class Airport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String city;
    private String country;
    private boolean active = true;
}
