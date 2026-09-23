package com.example.airline_booking_system.messaging.producer;

import com.example.airline_booking_system.common.exception.BookingProcessingException;
import com.example.airline_booking_system.messaging.event.KafkaEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(UUID eventId, String eventType, JsonNode payload) {

        try {
            String message = objectMapper.writeValueAsString(
                    new KafkaEvent(eventId, eventType, payload)
            );

            kafkaTemplate.send("booking-events", eventId.toString(), message).get();

        } catch (JsonProcessingException e) {
            throw new BookingProcessingException(
                    "Failed to serialize Kafka event");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BookingProcessingException("Kafka publishing was interrupted");
        } catch (ExecutionException e) {
            throw new BookingProcessingException(
                    "Failed to publish Kafka event");
        }
    }
}
