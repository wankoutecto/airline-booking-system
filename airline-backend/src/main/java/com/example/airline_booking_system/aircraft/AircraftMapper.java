package com.example.airline_booking_system.aircraft;

import com.example.airline_booking_system.aircraft.dto.AircraftAppendableResponse;
import com.example.airline_booking_system.aircraft.dto.AircraftResponse;
import com.example.airline_booking_system.airport.Airport;
import com.example.airline_booking_system.airport.AirportMapper;
import com.example.airline_booking_system.airport.dto.AirportResponse;
import com.example.airline_booking_system.flight.Flight;
import com.example.airline_booking_system.flight.dto.LatestFlightResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AircraftMapper {
    private final ModelMapper modelMapper;

    public AircraftResponse toAircraftResponse(Aircraft aircraft){
        AircraftResponse aircraftResponse = modelMapper.map(aircraft, AircraftResponse.class);
        aircraftResponse.setLocationAirport(aircraft.getLocationAirport().getCode());
        return aircraftResponse;
    }

    public  List<AircraftResponse> toAircraftResponseList(List<Aircraft> aircraftList){
        return aircraftList.stream()
                .map(this::toAircraftResponse)
                .toList();
    }

    public List<AircraftAppendableResponse> toAircraftAppendableResponseList(List<Aircraft> aircraftList,
                                                                   Map<Long, LatestFlightResponse> latestFlightMap){
        return aircraftList.stream()
                .map(aircraft -> AircraftAppendableResponse.builder()
                            .id(aircraft.getId())
                            .code(aircraft.getCode())
                            .locationAirport(aircraft.getLocationAirport().getCode())
                            .latestFlight(latestFlightMap.get(aircraft.getId()))
                            .build())
                .toList();
    }
}
