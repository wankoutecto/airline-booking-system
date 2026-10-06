package com.example.airline_booking_system.webhook;

import com.example.airline_booking_system.payment.Payment;
import com.example.airline_booking_system.payment.PaymentService;
import com.example.airline_booking_system.payment.PaymentStatus;
import com.example.airline_booking_system.webhook.dto.ProviderWebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final ProviderWebhookEventRepository providerWebhookEventRepository;
    private final PaymentService paymentService;

    @Transactional
    public void handleWebhook(ProviderWebhookRequest request){
        Payment payment = paymentService.findById(request.getPaymentId());
        PaymentStatus status = paymentService.mapToPaymentStatus(request.getEventType());

        paymentService.updatePaymentStatus(payment, status);

        ProviderWebhookEvent event = ProviderWebhookEvent.builder()
                .eventId(request.getEventId())
                .eventType(request.getEventType())
                .providerPaymentId(request.getProviderPaymentId())
                .providerRefundId(request.getProviderRefundId())
                .payment(payment)
                .receivedAt(LocalDateTime.now())
                .build();

        providerWebhookEventRepository.save(event);



    }
}
