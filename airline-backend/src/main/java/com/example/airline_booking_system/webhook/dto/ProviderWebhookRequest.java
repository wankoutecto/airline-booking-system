package com.example.airline_booking_system.webhook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProviderWebhookRequest {
    private String eventId;
    private String eventType;
    private String providerPaymentId;
    private Long paymentId;
    private String providerRefundId;
}
