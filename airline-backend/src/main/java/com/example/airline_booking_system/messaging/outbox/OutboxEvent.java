package com.example.airline_booking_system.messaging.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID eventId;
    private String eventType;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode payload;

    private LocalDateTime createdAt;
}
