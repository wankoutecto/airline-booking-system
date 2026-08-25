package com.example.airline_booking_system.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    @Value("${app.idempotency-expiration}")
    private Duration idempotencyExpiration;
    private IdempotencyRepository idempotencyRepository;
    public Idempotency createIdempotency(String key){
        return Idempotency.builder()
                .idempotencyKey(key)
                .status(IdempotencyStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(idempotencyExpiration))
                .build();
    }
}
