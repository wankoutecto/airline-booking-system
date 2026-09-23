package com.example.airline_booking_system.messaging.event;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record KafkaEvent(
        UUID eventId,
        String eventType,
        JsonNode payload
) {}
