package com.example.airline_booking_system.messaging.consumer;

import com.example.airline_booking_system.common.exception.BookingProcessingException;
import com.example.airline_booking_system.notification.NotificationEvent;
import com.example.airline_booking_system.notification.NotificationEventRepository;
import com.example.airline_booking_system.messaging.event.KafkaEvent;
import com.example.airline_booking_system.notification.NotificationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationEventRepository notificationEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "booking-events", groupId = "booking-service")
    public void consume(String message) {
        try {
            KafkaEvent kafkaEvent = objectMapper.readValue(message, KafkaEvent.class);
            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .createdAt(LocalDateTime.now())
                    .eventId(kafkaEvent.eventId())
                    .eventType(kafkaEvent.eventType())
                    .status(NotificationStatus.PENDING)
                    .payload(kafkaEvent.payload())
                    .build();


            notificationEventRepository.save(notificationEvent);

        } catch (JsonProcessingException e) {
            throw new BookingProcessingException(
                    "Failed to deserialize Kafka event");
        } catch (DataIntegrityViolationException ex){
            if(isDuplicateEventId(ex)) {
                log.info("Event already processed", ex);
                return;
            }

            throw ex;
        }
    }
    //Detect unique violation event_id (postgresql code = 23505)
    private boolean isDuplicateEventId(DataIntegrityViolationException ex) {
        Throwable cause = ex;

        while (cause != null) {
            if (cause instanceof PSQLException psqlException) {
                ServerErrorMessage serverError = psqlException.getServerErrorMessage();
                return "23505".equals(psqlException.getSQLState())
                        && serverError != null
                        && "notification_event_event_id_key".equals(
                        serverError.getConstraint()
                );
            }

            cause = cause.getCause();
        }

        return false;
    }
}