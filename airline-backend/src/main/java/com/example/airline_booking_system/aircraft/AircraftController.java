package com.example.airline_booking_system.aircraft;

import com.example.airline_booking_system.aircraft.dto.AircraftRequest;
import com.example.airline_booking_system.aircraft.dto.AircraftResponse;
import com.example.airline_booking_system.aircraft.enums.AircraftStatus;
import com.example.airline_booking_system.airport.Airport;
import com.example.airline_booking_system.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/aircraft")
@RequiredArgsConstructor
public class AircraftController {

    private final AircraftService aircraftService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<AircraftResponse>> create(@Valid
            @RequestBody AircraftRequest aircraft) {

        try {
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            aircraftService.createAircraft(aircraft),
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(null, e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AircraftResponse>> get(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        aircraftService.getAircraftResponse(id),
                        null
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<AircraftResponse>>> all() {

        try {
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            aircraftService.getAllAircraft(),
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(null, e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AircraftResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam AircraftStatus status) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        aircraftService.updateStatus(id, status),
                        null
                )
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/location")
    public ResponseEntity<ApiResponse<AircraftResponse>> updateLocation(
            Long id,
           @RequestBody Airport airport) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        aircraftService.updateLocation(id, airport),
                        null
                )
        );
    }
}
