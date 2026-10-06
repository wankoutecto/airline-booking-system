package com.example.airline_booking_system.webhook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderWebhookEventRepository extends JpaRepository<ProviderWebhookEvent, Long> {
}
