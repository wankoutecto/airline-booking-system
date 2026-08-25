package com.example.airline_booking_system.airport.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirportResponse {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String country;
    private boolean active;
}
