package com.example.airline_booking_system.idempotency;

import com.example.airline_booking_system.common.entity.CreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdempotencyRecord extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    private String idempotencyStatus;
    private Long bookingId;
}
