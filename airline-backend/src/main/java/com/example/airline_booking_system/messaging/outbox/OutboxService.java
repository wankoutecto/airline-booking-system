package com.example.airline_booking_system.messaging.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository outboxEventRepository;

    public void createEvent(String eventType, JsonNode payload){
        OutboxEvent event = OutboxEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType(eventType)
                .payload(payload)
                .status(OutboxStatus.UNPUBLISHED)
                .createdAt(LocalDateTime.now())
                .build();
        outboxEventRepository.save(event);
    }
}
