package com.example.airline_booking_system.webhook;

import com.example.airline_booking_system.common.response.ApiResponse;
import com.example.airline_booking_system.webhook.dto.ProviderWebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;

    public ResponseEntity<ApiResponse<Void>> handleWebhook(ProviderWebhookRequest request){
        webhookService.handleWebhook(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(null, "Webhook handled"));
    }

}
