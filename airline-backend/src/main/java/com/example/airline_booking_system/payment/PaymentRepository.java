package com.example.airline_booking_system.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findById(Long paymentId);
}
