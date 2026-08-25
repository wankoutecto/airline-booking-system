package com.example.airline_booking_system.auth;

import com.example.airline_booking_system.auth.dto.LoginRequest;
import com.example.airline_booking_system.auth.dto.LoginResponse;
import com.example.airline_booking_system.auth.dto.RefreshRequest;
import com.example.airline_booking_system.auth.dto.RegisterRequest;
import com.example.airline_booking_system.common.exception.ResourceAlreadyExistsException;
import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import com.example.airline_booking_system.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(null, "Successful register"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(response, "Successful login"));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshRequest request){
        LoginResponse response = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(response, "Successful refresh"));
    }
}
