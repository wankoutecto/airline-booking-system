package com.example.airline_booking_system.common.exception;

import com.example.airline_booking_system.common.response.ApiResponse;
import com.example.airline_booking_system.security.refresh.InvalidRefreshTokenException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(null, ex.getMessage()));
    }
    @ExceptionHandler(ResourceNotAvailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotAvailable(
            ResourceNotAvailableException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(null, ex.getMessage()));
    }
    @ExceptionHandler(InvalidFlightRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidFlight(
            InvalidFlightRequestException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(null, ex.getMessage()));
    }
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRefreshToke(
            InvalidFlightRequestException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(null, ex.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(null, message));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(null, "Invalid email or password"));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(
            UsernameNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(
                        null,
                        "Invalid username or password"
                ));
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(
            NoHandlerFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(null,
                        "The requested endpoint does not exist"));
    }
    @ExceptionHandler(BookingProcessingException.class)
    public ResponseEntity<?> handleBookingProcessingException(
            BookingProcessingException ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        null,
                        ex.getMessage()
                ));

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDatabaseConstraint(DataIntegrityViolationException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(null, "Database constraint violation"));
    }
}
