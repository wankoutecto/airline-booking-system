package com.example.airline_booking_system.aircraft;

import com.example.airline_booking_system.aircraft.dto.AircraftRequest;
import com.example.airline_booking_system.aircraft.dto.AircraftResponse;
import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import com.example.airline_booking_system.airport.Airport;
import com.example.airline_booking_system.airport.AirportRepository;
import com.example.airline_booking_system.common.exception.ResourceAlreadyExistsException;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {


    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;
    private final AircraftMapper aircraftMapper;

    public AircraftService(AircraftRepository aircraftRepository, AirportRepository airportRepository, AircraftMapper aircraftMapper) {
        this.aircraftRepository = aircraftRepository;
        this.airportRepository = airportRepository;
        this.aircraftMapper = aircraftMapper;
    }


    public AircraftResponse createAircraft(AircraftRequest request){

        if(aircraftRepository.existsByCode(request.getCode())){
            throw new ResourceAlreadyExistsException(
                    "Aircraft already exists");
        }
        if(!airportRepository.existsByCode(request.getLocationAirport())){
            throw new IllegalArgumentException(
                    "Not a valid airport code");
        }
        Airport airport = airportRepository.findByCode(request.getLocationAirport())
                .orElseThrow(()-> new ResourceNotFoundException("Airport not found"));

        Aircraft aircraft = Aircraft.builder()
                .code(request.getCode())
                .status(AircraftStatus.AVAILABLE)
                .locationAirport(airport)
                .build();

        return aircraftMapper.toAircraftResponse(aircraftRepository.save(aircraft));
    }

    public Aircraft getAircraft(Long id){

        return aircraftRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Aircraft not found"));
    }
    public AircraftResponse getAircraftResponse(Long id){

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Aircraft not found"));
        return aircraftMapper.toAircraftResponse(aircraft);
    }


    public List<AircraftResponse> getAllAircraft(){

        return aircraftMapper.toAircraftResponseList(aircraftRepository.findAll());
    }


    public AircraftResponse updateStatus(
            Long id,
            AircraftStatus status){

        Aircraft aircraft = getAircraft(id);

        aircraft.setStatus(status);

        return aircraftMapper.toAircraftResponse(aircraftRepository.save(aircraft));
    }


    public AircraftResponse updateLocation(
            Long id,
            Airport airport){

        Aircraft aircraft = getAircraft(id);

        aircraft.setLocationAirport(airport);

        return aircraftMapper.toAircraftResponse(aircraftRepository.save(aircraft));
    }
}
