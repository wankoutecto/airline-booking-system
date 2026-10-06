package com.example.airline_booking_system.refund;

import com.example.airline_booking_system.common.entity.BaseEntity;
import com.example.airline_booking_system.payment.Payment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Refund extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String orderType;
    private Long amount;
    private String idempotencyKey;
    private String providerPaymentId;
    private String providerRefundId;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
