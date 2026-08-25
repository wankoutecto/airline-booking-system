package com.example.airline_booking_system.flight;

import com.example.airline_booking_system.flight.dto.FlightResponse;
import com.example.airline_booking_system.flight.dto.LatestFlightResponse;
import com.example.airline_booking_system.flight.flightSeat.FlightSeat;
import com.example.airline_booking_system.flight.flightSeat.FlightSeatResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    private final ModelMapper modelMapper;

    public FlightResponse toFlightResponse(Flight flight) {

        FlightResponse response = modelMapper.map(flight, FlightResponse.class);

        response.setAircraftCode(flight.getAircraft().getCode());

        response.setDepartureAirportCode(flight.getDepartureAirport().getCode());

        response.setArrivalAirportCode(flight.getArrivalAirport().getCode());

        return response;
    }

    public List<FlightResponse> toFlightResponseList(List<Flight> flights){
        return flights.stream().map(this::toFlightResponse).toList();
    }
    public LatestFlightResponse toLatestFlightResponse(Flight flight){

        return LatestFlightResponse.builder()
                .flightNumber(flight.getFlightNumber())
                .arrivalTime(flight.getArrivalTime())
                .arrivalAirportCode(flight.getArrivalAirport().getCode())
                .build();
    }


    public List<FlightSeatResponse> toFlightSeatList(List<FlightSeat> flightSeats) {
        return flightSeats.stream().map(flightSeat -> {
            return FlightSeatResponse.builder()
                    .seat(flightSeat.getSeatNumber())
                    .status(flightSeat.getStatus().name())
                    .seatClass(flightSeat.getSeatClass().name())
                    .build();
        }).toList();
    }
}
