package com.example.airline_booking_system;

import com.example.airline_booking_system.airport.Airport;
import com.example.airline_booking_system.airport.AirportMapper;
import com.example.airline_booking_system.airport.AirportRepository;
import com.example.airline_booking_system.airport.AirportService;
import com.example.airline_booking_system.airport.dto.AirportRequest;
import com.example.airline_booking_system.airport.dto.AirportResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AirportServiceUnitTest {
    @Mock
    AirportRepository airportRepository;
    @Mock
    AirportMapper airportMapper;
    @Mock
    AirportRequest request;
    @Mock
    AirportResponse response;
    @InjectMocks
    AirportService airportService;


    @Test
    void shouldCreateAirport(){
        when(request.getCode()).thenReturn("IAD");
        when(request.getCity()).thenReturn("Washington");
        when(request.getName()).thenReturn("Dulles International Airport");
        when(request.getCountry()).thenReturn("USA");

        when(airportRepository.existsByCode(request.getCode())).thenReturn(false);

        Airport airport = Airport.builder()
                .id(1L)
                .code(request.getCode())
                .city(request.getCity())
                .name(request.getName())
                .country(request.getCountry())
                .active(true)
                .build();

        when(airportRepository.save(any(Airport.class))).thenReturn(airport);

        when(airportMapper.toAirportResponse(airport)).thenReturn(response);

        AirportResponse result = airportService.createAirport(request);

        assertEquals(response, result);
    }

    void shouldThrowException(){
        when(request.getCode()).thenReturn("IAD");
        when(airportRepository.existsByCode(request.getCode())).thenReturn(true);
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> airportService.createAirport(request));

    }
}
