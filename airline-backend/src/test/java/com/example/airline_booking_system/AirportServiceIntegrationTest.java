package com.example.airline_booking_system;

import com.example.airline_booking_system.airport.AirportRepository;
import com.example.airline_booking_system.airport.AirportService;
import com.example.airline_booking_system.airport.dto.AirportRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class AirportServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    @Autowired
    AirportService airportService;
    @Autowired
    AirportRepository airportRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldCreateAirport(){
        AirportRequest request = new AirportRequest();
        request.setCode("XYZ");
        request.setCity("Washington");
        request.setName("Dulles International Airport");
        request.setCountry("USA");

        airportService.createAirport(request);

       assertTrue(airportRepository.findByCode(request.getCode()).isPresent());
    }
}
