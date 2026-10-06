package com.example.airline_booking_system.payment;

import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    public Payment createPayment(Long orderId, String orderType, Long amount, String idempotencyKey, String currency){

        Payment payment = Payment.builder()
                .orderId(orderId)
                .orderType(orderType)
                .amount(amount)
                .currency(currency)
                .idempotencyKey(idempotencyKey)
                .status(PaymentStatus.PAYMENT_PENDING)
                .build();

       return paymentRepository.save(payment);


    }
    public void updatePaymentStatus(Payment payment, PaymentStatus status){
        PaymentStatus currentStatus = payment.getStatus();
        boolean allowed = false;

        if(currentStatus == PaymentStatus.PAYMENT_PENDING &&
                status != PaymentStatus.PAYMENT_PENDING){
            allowed = true;

        }else if(currentStatus == PaymentStatus.PAYMENT_PROCESSING &&
                (status == PaymentStatus.PAYMENT_SUCCEEDED || status == PaymentStatus.PAYMENT_FAILED)){
            allowed = true;
        }

        if(allowed){
            payment.setStatus(status);
            paymentRepository.save(payment);
        }
    }


    //helper method
    public Payment findById(Long paymentId){
        return paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }


    public PaymentStatus mapToPaymentStatus(String status){
        return switch (status) {
            case "payment.succeeded" -> PaymentStatus.PAYMENT_SUCCEEDED;
            case "payment.failed" -> PaymentStatus.PAYMENT_FAILED;
            case "payment.processing" -> PaymentStatus.PAYMENT_PROCESSING;
            case "payment.pending" -> PaymentStatus.PAYMENT_PENDING;
            default -> throw new IllegalArgumentException("Not a valid payment status");
        };
    }
}
