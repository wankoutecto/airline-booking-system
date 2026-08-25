package com.example.airline_booking_system.airport;

import com.example.airline_booking_system.airport.dto.AirportRequest;
import com.example.airline_booking_system.airport.dto.AirportResponse;
import com.example.airline_booking_system.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/airports")
@RequiredArgsConstructor
public class AirportController {


    private final AirportService airportService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<AirportResponse>> create(
            @Valid @RequestBody AirportRequest airport){

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(airportService.createAirport(airport), null)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> get(
            @PathVariable Long id){

        return ResponseEntity.ok(
                new ApiResponse<>(airportService.getAirportResponse(id), null)
        );
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<AirportResponse>> findAirport(
            @PathVariable String code){
        return ResponseEntity.ok(
                new ApiResponse<>(airportService.findAirport(code), null)
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<AirportResponse>>> getAll(){
        return ResponseEntity.ok(
                new ApiResponse<>(airportService.getAllAirports(), null)
        );
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> update(
            @PathVariable Long id,
            @RequestBody Airport airport){

        return ResponseEntity.ok(
                new ApiResponse<>(airportService.updateAirport(id, airport), null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id){

        airportService.deactivateAirport(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
