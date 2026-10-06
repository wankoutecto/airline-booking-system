package com.example.airline_booking_system.payment;

import com.example.airline_booking_system.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String orderType;
    private Long amount;
    private String currency;
    private String idempotencyKey;
    private String providerPaymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
