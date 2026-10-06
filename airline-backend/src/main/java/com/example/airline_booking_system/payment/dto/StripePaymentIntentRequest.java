package com.example.airline_booking_system.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StripePaymentIntentRequest {
    private Long amount;
    private String currency;
}
