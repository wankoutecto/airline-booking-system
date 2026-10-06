package com.example.airline_booking_system.webhook;

import com.example.airline_booking_system.payment.Payment;
import com.example.airline_booking_system.refund.Refund;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ProviderWebhookEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventId;
    private String eventType;
    private String providerPaymentId;
    private String providerRefundId;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "refund_id")
    private Refund refund;

    private LocalDateTime receivedAt;
}
