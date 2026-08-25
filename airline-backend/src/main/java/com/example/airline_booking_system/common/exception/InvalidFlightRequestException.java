package com.example.airline_booking_system.common.exception;

public class InvalidFlightRequestException extends RuntimeException {
    public InvalidFlightRequestException(String message) {
        super(message);
    }
}
