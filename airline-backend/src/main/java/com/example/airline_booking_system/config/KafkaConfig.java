package com.example.airline_booking_system.config;

import com.example.airline_booking_system.common.exception.BookingProcessingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConfig {

    // 1. Define the error-handling behavior
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            KafkaTemplate<String, String> kafkaTemplate) {

        DeadLetterPublishingRecoverer recover =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        ExponentialBackOff backOff =
                new ExponentialBackOff(1000L, 2.0);

        //maximum elapsed-time limit on the backoff/retry sequence.
        backOff.setMaxElapsedTime(10_000L);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recover, backOff);

        errorHandler.addNotRetryableExceptions(
                BookingProcessingException.class
        );

        return errorHandler;
    }

    // 2. Attach the error handler to the Kafka listener container
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            DefaultErrorHandler kafkaErrorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);

        return factory;
    }
}