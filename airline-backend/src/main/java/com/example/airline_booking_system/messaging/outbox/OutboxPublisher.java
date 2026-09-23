package com.example.airline_booking_system.messaging.outbox;

import com.example.airline_booking_system.common.exception.BookingProcessingException;
import com.example.airline_booking_system.messaging.producer.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaMessagePublisher kafkaMessagePublisher;

    @Scheduled(fixedDelay = 5000)
    public void publisherEvent(){
        List<OutboxEvent> events = outboxEventRepository.findAllByStatus(OutboxStatus.UNPUBLISHED);
        for (OutboxEvent event : events) {

            try {
                kafkaMessagePublisher.publish(
                        event.getEventId(),
                        event.getEventType(),
                        event.getPayload()
                );

                event.setStatus(OutboxStatus.PUBLISHED);
                outboxEventRepository.save(event);
            } catch (BookingProcessingException e) {
                log.error("Failed to publish event {}", event.getEventId(), e);
            }
        }
    }
}
