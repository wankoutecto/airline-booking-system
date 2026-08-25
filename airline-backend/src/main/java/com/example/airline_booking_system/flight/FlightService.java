package com.example.airline_booking_system.flight;

import com.example.airline_booking_system.aircraft.Aircraft;
import com.example.airline_booking_system.aircraft.AircraftMapper;
import com.example.airline_booking_system.aircraft.AircraftRepository;
import com.example.airline_booking_system.aircraft.aircraftSeat.AircraftSeat;
import com.example.airline_booking_system.aircraft.aircraftSeat.AircraftSeatRepository;
import com.example.airline_booking_system.aircraft.dto.AircraftAvailableRequest;
import com.example.airline_booking_system.aircraft.dto.AircraftAvailableResponse;
import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import com.example.airline_booking_system.airport.Airport;
import com.example.airline_booking_system.airport.AirportRepository;
import com.example.airline_booking_system.airport.AirportService;
import com.example.airline_booking_system.common.exception.InvalidFlightRequestException;
import com.example.airline_booking_system.common.exception.ResourceNotAvailableException;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import com.example.airline_booking_system.flight.dto.CreateFlightRequest;
import com.example.airline_booking_system.flight.dto.FlightAvailableRequest;
import com.example.airline_booking_system.flight.dto.FlightResponse;
import com.example.airline_booking_system.flight.dto.LatestFlightResponse;
import com.example.airline_booking_system.flight.enums.FlightSeatStatus;
import com.example.airline_booking_system.flight.enums.FlightStatus;
import com.example.airline_booking_system.flight.flightSeat.FlightSeat;
import com.example.airline_booking_system.flight.flightSeat.FlightSeatRepository;
import com.example.airline_booking_system.flight.flightSeat.FlightSeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final AircraftRepository aircraftRepository;
    private final AirportService airportService;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final FlightMapper flightMapper;
    private final AircraftSeatRepository aircraftSeatRepository;
    private final AircraftMapper aircraftMapper;


    public AircraftAvailableResponse findAvailableAircraft(AircraftAvailableRequest request) {

        Airport departureAirport = airportService.getAirportByCode(request.getDepartureAirport());

        Airport arrivalAirport = airportService.getAirportByCode(request.getArrivalAirport());

        validateTime(request.getDepartureTime(), request.getArrivalTime());
        validateRoute(departureAirport.getCode(), arrivalAirport.getCode());

        List<Aircraft> homeBaseAircraft =
                findHomeBaseAircraft(departureAirport.getId(), AircraftStatus.AVAILABLE);

        List<Aircraft> appendableAircraft =
                findAppendableAircraft(departureAirport.getId(),
                        request.getDepartureTime(),
                        AircraftStatus.AVAILABLE);

        List<Long> aircraftIds = appendableAircraft.stream().map(Aircraft::getId).toList();
        List<Flight> latestFlights = flightRepository.findLatestFlightByAircraftIds(aircraftIds);
        Map<Long, LatestFlightResponse> latestFlightMap = latestFlights.stream()
                .collect(Collectors.toMap(flight -> flight.getAircraft().getId(),
                        flightMapper::toLatestFlightResponse
                ));

        return new AircraftAvailableResponse(aircraftMapper.toAircraftResponseList(homeBaseAircraft),
                aircraftMapper.toAircraftAppendableResponseList(appendableAircraft, latestFlightMap));
    }

    @Transactional
    public FlightResponse createFlight(CreateFlightRequest request){

        Aircraft aircraft = aircraftRepository.findByIdForUpdate(request.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found"));

        Airport departureAirport = airportService.getAirportByCode(request.getDepartureAirport());

        Airport arrivalAirport = airportService.getAirportByCode(request.getArrivalAirport());

        validateTime(request.getDepartureTime(), request.getArrivalTime());
        validateRoute(departureAirport.getCode(), arrivalAirport.getCode());

        validateAirportStatus(departureAirport, arrivalAirport);
        validateAircraftForFlight(aircraft, departureAirport, request.getDepartureTime());


        Flight flight = buildFlight(request, aircraft, departureAirport, arrivalAirport);
        flightRepository.save(flight);

        List<FlightSeat> flightSeats = generateFlightSeat(flight);
        flightSeatRepository.saveAll(flightSeats);

        flight.setFlightSeats(flightSeats);
        return flightMapper.toFlightResponse(flight);
    }


    public FlightResponse getFlightResponse(Long id){

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        return flightMapper.toFlightResponse(flight);
    }

    public List<FlightResponse> getAllFlight(){

        return flightMapper.toFlightResponseList(flightRepository.findAll());
    }

    public List<FlightResponse> getAllAircraftFlight(Long aircraftId){

        return flightMapper.toFlightResponseList(flightRepository.findAllByAircraftId(aircraftId));
    }

    public List<FlightResponse> findAvailableFlights(FlightAvailableRequest request) {
        validateRoute(request.getDepartureAirport(), request.getArrivalAirport());
        validateTime(request.getDepartureDate().atStartOfDay(), request.getArrivalDate().atStartOfDay());

        airportService.checkAirportExist(request.getDepartureAirport());
        airportService.checkAirportExist(request.getArrivalAirport());

        List<Flight> flights = flightRepository.findBookableFlights(request.getDepartureAirport(),
                request.getArrivalAirport(),
                request.getDepartureDate().atStartOfDay());
        return flightMapper.toFlightResponseList(flights);
    }









    //Helper method
    public List<FlightSeat> generateFlightSeat(Flight flight){

        List<AircraftSeat> aircraftSeats = aircraftSeatRepository
                .findByAircraftId(flight.getAircraft().getId());

        return aircraftSeats.stream().map(seat -> FlightSeat.builder()
                .flight(flight)
                .seatNumber(seat.getSeatNumber())
                .seatClass(seat.getSeatClass())
                .status(FlightSeatStatus.AVAILABLE)
                .build()).toList();
    }
    private Flight buildFlight(CreateFlightRequest request,
                               Aircraft aircraft,
                               Airport departureAirport,
                               Airport arrivalAirport) {

        return Flight.builder()
                .flightNumber(request.getFlightNumber())
                .aircraft(aircraft)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .status(FlightStatus.SCHEDULED)
                .build();
    }

    private void validateTime(LocalDateTime departure, LocalDateTime arrival) {
        if(departure.isBefore(LocalDateTime.now()) ||
                arrival.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException(
                    "Departure and arrival must be after current date and time"
            );
        }
        if (arrival.isBefore(departure)) {
            throw new IllegalArgumentException(
                    "Arrival time must be after departure time"
            );
        }
    }
    public void validateAircraftForFlight(Aircraft aircraft,
                                          Airport departureAirport,
                                          LocalDateTime departureTime){
        validateAircraftStatus(aircraft);
        validateAircraftHasSeat(aircraft);
        validateAircraftSchedule(aircraft, departureAirport, departureTime);
    }

    private void validateAircraftHasSeat(Aircraft aircraft){

        if(!aircraftSeatRepository.existsByAircraftId(aircraft.getId())){
            throw new ResourceNotAvailableException(
                    "Aircraft does not have seats configured"
            );
        }
    }
    private void validateAircraftSchedule(Aircraft aircraft,
                                          Airport departureAirport,
                                          LocalDateTime departureTime) {
        Optional<Flight> latestFlight =
                flightRepository.findAircraftLatestFlight(aircraft.getId());

        if (latestFlight.isEmpty()) {
            validateAircraftLocation(aircraft, departureAirport);
            return;
        }

        Flight flight = latestFlight.get();

        if (!flight.getArrivalAirport().getCode()
                .equalsIgnoreCase(departureAirport.getCode())) {
            throw new InvalidFlightRequestException(
                    "Aircraft's last flight does not end at the departure airport");
        }

        if (flight.getArrivalTime().isAfter(departureTime)) {
            throw new InvalidFlightRequestException(
                    "Aircraft's last flight arrives after the new departure time" );
        }
    }
    private void validateAircraftStatus(Aircraft aircraft) {

        if(aircraft.getStatus() != AircraftStatus.AVAILABLE){
            throw new ResourceNotAvailableException("Aircraft is not available");
        }
    }
    public void validateAirportStatus(Airport departureAirport, Airport arrivalAirport){

        if(!departureAirport.isActive() || !arrivalAirport.isActive()){
            throw new ResourceNotAvailableException("Airport not active");
        }
    }

    public void validateRoute(String departureAirport, String arrivalAirport){

        if(departureAirport.equalsIgnoreCase(arrivalAirport)){
            throw new InvalidFlightRequestException("Departure and arrival must be different");
        }
    }

    public void validateAircraftLocation(Aircraft aircraft, Airport departureAirport){

        if(!aircraft.getLocationAirport().getCode().equalsIgnoreCase(departureAirport.getCode())){
            throw new InvalidFlightRequestException("Aircraft is not located at the departure airport");
        }
    }

    public List<Aircraft> findHomeBaseAircraft(Long departureId, AircraftStatus status) {

        return aircraftRepository.findAircraftHomeBase(departureId, status);
    }
    public List<Aircraft> findAppendableAircraft(Long departureId,
                                                 LocalDateTime departureTime,
                                                 AircraftStatus status) {
        return aircraftRepository.findAircraftAppendable(departureId, departureTime, status);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    public List<FlightSeatResponse> getFlightSeats(Long flightId) {
        List<FlightSeat> flightSeats = flightSeatRepository.findAllByFlightId(flightId);
        return flightMapper.toFlightSeatList(flightSeats);
    }
}
