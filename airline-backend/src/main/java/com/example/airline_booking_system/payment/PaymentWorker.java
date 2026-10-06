package com.example.airline_booking_system.payment;

import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PaymentWorker {
    private final PaymentRepository paymentRepository;
    private final PaymentProvider paymentProvider;
    public void processPendingPayment(){
        /*
        Payment payment = paymentRepository.findByStatus(PaymentStatus.PAYMENT_PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        PaymentProviderResponse response = paymentProvider.processPayment(payment);

        payment.setProviderPaymentId(response.getProviderPaymentId());
        payment.setStatus(response.getStatus());

        paymentRepository.save(payment);

         */
    }
}
