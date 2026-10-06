package com.example.airline_booking_system.payment.dto;

import com.example.airline_booking_system.payment.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StripePaymentIntentResponse {
    private String id;
    private String status;

    @JsonProperty("client_secret")
    private String clientSecret;
}
