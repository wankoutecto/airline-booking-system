package com.example.airline_booking_system.airport;

import com.example.airline_booking_system.airport.dto.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AirportMapper {
    private final ModelMapper modelMapper;
    public AirportResponse toAirportResponse(Airport airport){
        return modelMapper.map(airport, AirportResponse.class);
    }
    public List<AirportResponse> toAirportResponseList(List<Airport> airportList){
        return airportList.stream()
                .map(this::toAirportResponse)
                .toList();
    }
}
