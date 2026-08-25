package com.example.airline_booking_system.aircraft.aircraftSeat;

import com.example.airline_booking_system.aircraft.aircraftSeat.dto.AircraftSeatLayoutResponse;
import com.example.airline_booking_system.aircraft.aircraftSeat.dto.GenerateAircraftSeatsRequest;
import com.example.airline_booking_system.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/{aircraftId}/seats")
@RequiredArgsConstructor
public class AircraftSeatController {

    private final AircraftSeatService aircraftSeatService;

    @PostMapping
    public ResponseEntity<ApiResponse<AircraftSeatLayoutResponse>> generateSeats(
        @PathVariable Long aircraftId,
        @RequestBody GenerateAircraftSeatsRequest request){
        return ResponseEntity.ok(new ApiResponse<>(aircraftSeatService
                .generateSeats(aircraftId, request), "Seats created")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AircraftSeat>>> getSeats(@PathVariable Long aircraftId){

        return ResponseEntity.ok(new ApiResponse<>(
                aircraftSeatService.getSeats(aircraftId), null)
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAllAircraftSeat(@PathVariable Long aircraftId){

        aircraftSeatService.deleteAllAircraftSeat(aircraftId);

        return ResponseEntity.noContent().build();
    }
}
