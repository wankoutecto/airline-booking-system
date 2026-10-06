package com.example.airline_booking_system.payment;

import com.example.airline_booking_system.payment.dto.StripePaymentIntentRequest;
import com.example.airline_booking_system.payment.dto.StripePaymentIntentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class StripePaymentProvider {

    private final RestClient stripeRestClient;

    public StripePaymentIntentResponse createPaymentIntent(StripePaymentIntentRequest request, String idempotencyKey){

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("amount", request.getAmount().toString());
        formData.add("currency", request.getCurrency());

        return stripeRestClient.post()
                .uri("/v1/payment_intents")
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(StripePaymentIntentResponse.class);
    }

}
