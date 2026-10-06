package com.example.airline_booking_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class StripeConfig {

    @Bean
    public RestClient stripeRestClient(@Value("${stripe.secret-key}") String secretKey) {

        return RestClient.builder()
                .baseUrl("https://api.stripe.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .build();
    }
}
