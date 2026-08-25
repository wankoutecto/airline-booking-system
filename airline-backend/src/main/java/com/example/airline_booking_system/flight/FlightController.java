package com.example.airline_booking_system.flight;

import com.example.airline_booking_system.aircraft.dto.AircraftAvailableRequest;
import com.example.airline_booking_system.aircraft.dto.AircraftAvailableResponse;
import com.example.airline_booking_system.common.response.ApiResponse;
import com.example.airline_booking_system.flight.dto.CreateFlightRequest;
import com.example.airline_booking_system.flight.dto.FlightAvailableRequest;
import com.example.airline_booking_system.flight.dto.FlightResponse;
import com.example.airline_booking_system.flight.flightSeat.FlightSeat;
import com.example.airline_booking_system.flight.flightSeat.FlightSeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createFlight(@Valid @RequestBody CreateFlightRequest request){
        FlightResponse flight = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(null, "Flight " + flight.getFlightNumber() + " is created"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlight(Long id){
        FlightResponse response = flightService.getFlightResponse(id);
        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getAllFlight(
            @RequestParam(required = false) Long aircraftId){
        if(aircraftId == null){
            return ResponseEntity.ok(new ApiResponse<>(flightService.getAllFlight(), null));
        }
        return ResponseEntity.ok(new ApiResponse<>(flightService.getAllAircraftFlight(aircraftId), null));
    }

    @PostMapping("/available-aircraft")
    public ResponseEntity<ApiResponse<AircraftAvailableResponse>> findAvailableAircraft(
            @Valid @RequestBody AircraftAvailableRequest request
            ){
        try {
            AircraftAvailableResponse response = flightService.findAvailableAircraft(request);
            return ResponseEntity.ok(new ApiResponse<>(response, "List of aircraft available"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(null, e.getMessage()));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public  ResponseEntity<ApiResponse<Void>> deleteFlight(@PathVariable Long id){
        flightService.deleteFlight(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "Flight deleted"));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<FlightResponse>>> findAvailableFlights(
            @Valid @RequestBody FlightAvailableRequest request){
        List<FlightResponse> response = flightService.findAvailableFlights(request);

        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }
    @GetMapping("/{id}/seats")
    public ResponseEntity<ApiResponse<List<FlightSeatResponse>>> getFlightSeats(@PathVariable("id") Long flightId){
        List<FlightSeatResponse> response = flightService.getFlightSeats(flightId);

        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }


}
