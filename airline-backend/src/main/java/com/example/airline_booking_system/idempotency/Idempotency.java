package com.example.airline_booking_system.idempotency;

import com.example.airline_booking_system.common.entity.CreatedEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Idempotency{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status;

    private String requestHash;
    private Integer responseStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode responseBody;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
