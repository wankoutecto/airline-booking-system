package com.example.airline_booking_system.idempotency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyRepository extends JpaRepository<Idempotency, Long> {
    Optional<Idempotency> findByIdempotencyKey(String idempotency);
}
