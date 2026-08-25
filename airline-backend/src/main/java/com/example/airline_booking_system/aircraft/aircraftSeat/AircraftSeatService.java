package com.example.airline_booking_system.aircraft.aircraftSeat;

import com.example.airline_booking_system.aircraft.Aircraft;
import com.example.airline_booking_system.aircraft.AircraftRepository;
import com.example.airline_booking_system.aircraft.aircraftSeat.dto.AircraftSeatLayoutResponse;
import com.example.airline_booking_system.aircraft.aircraftSeat.dto.GenerateAircraftSeatsRequest;
import com.example.airline_booking_system.aircraft.aircraftSeat.dto.SeatConfigurationRequest;
import com.example.airline_booking_system.aircraft.enums.SeatClass;
import com.example.airline_booking_system.common.exception.ResourceAlreadyExistsException;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AircraftSeatService {
    private final AircraftSeatRepository aircraftSeatRepository;
    private final AircraftRepository aircraftRepository;
    private static final String SEAT_LETTER_PATTERN = "^[A-Z]$";


    @Transactional
    public AircraftSeatLayoutResponse generateSeats(Long aircraftId, GenerateAircraftSeatsRequest request){
        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft not found"));
        if(aircraftSeatRepository.existsByAircraftId(aircraftId)){
            throw new ResourceAlreadyExistsException("Aircraft already has a seat layout");
        }

        validateRequest(request.getSeatConfigurations());

        List<AircraftSeat> seats = new ArrayList<>();

        for (SeatConfigurationRequest config : request.getSeatConfigurations()) {

            for (int row = config.getStartRow(); row <= config.getEndRow(); row++) {

                for (String letter : config.getSeatLetters()) {

                    AircraftSeat seat = AircraftSeat.builder()
                            .aircraft(aircraft)
                            .seatNumber(row + letter.toUpperCase())
                            .seatClass(config.getSeatClass())
                            .build();

                    seats.add(seat);
                }
            }
        }

        aircraftSeatRepository.saveAll(seats);

        Map<SeatClass, Long> seatPerClass = seats.stream()
                .collect(Collectors.groupingBy(AircraftSeat::getSeatClass, Collectors.counting()));

        return AircraftSeatLayoutResponse.builder()
                .aircraftId(aircraftId)
                .totalSeats(seats.size())
                .seatsPerClass(seatPerClass)
                .build();
    }

    public List<AircraftSeat> getSeats(
            Long aircraftId){

        return aircraftSeatRepository
                .findByAircraftId(aircraftId);
    }



    public void deleteAllAircraftSeat(Long aircraftId){

        if(!aircraftSeatRepository.existsByAircraftId(aircraftId)){
            throw new ResourceNotFoundException(
                    "Aircraft seats not found");
        }

        aircraftSeatRepository.deleteAllByAircraftId(aircraftId);
    }



    //Validate request
    public void validateRequest(List<SeatConfigurationRequest> request){

        if(request == null || request.isEmpty()){
            throw new IllegalArgumentException(
                    "Aircraft must have at least 1 seat");
        }

        Set<Integer> usedRows = new HashSet<>();

        for(SeatConfigurationRequest config : request){

            if(config.getStartRow() > config.getEndRow()){
                throw new IllegalArgumentException(
                        "Start row can't be greater than end row");
            }

            for(int row = config.getStartRow(); row <= config.getEndRow(); row++){

                if(!usedRows.add(row)){
                    throw new IllegalArgumentException(
                            "Row range overlapping: " + row);
                }
            }

            Set<String> seatLetters = new HashSet<>();

            for(String letter : config.getSeatLetters()){

                String normalizedLetter = letter.toUpperCase();

                if(!normalizedLetter.matches(SEAT_LETTER_PATTERN)){
                    throw new IllegalArgumentException(
                            "Invalid seat letter: " + letter);
                }

                if(!seatLetters.add(normalizedLetter)){
                    throw new IllegalArgumentException(
                            "Duplicate seat letter: " + letter +
                                    " in " + config.getSeatClass() + " class");
                }
            }
        }
    }
}
