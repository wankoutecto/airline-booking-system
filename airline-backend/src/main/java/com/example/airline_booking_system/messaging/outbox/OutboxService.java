package com.example.airline_booking_system.messaging.outbox;

import com.example.airline_booking_system.messaging.event.AggregateType;
import com.example.airline_booking_system.messaging.event.EventType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository outboxEventRepository;

    public void createEvent(EventType eventType, Long aggregateId, AggregateType aggregateType, JsonNode payload){
        OutboxEvent event = OutboxEvent.builder()
                .eventId(UUID.randomUUID())
                .aggregateType(aggregateType.name())
                .aggregateId(aggregateId)
                .eventType(eventType.name())
                .payload(payload)
                .status(OutboxStatus.UNPUBLISHED)
                .createdAt(LocalDateTime.now())
                .build();
        outboxEventRepository.save(event);
    }
}
