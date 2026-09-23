package com.example.airline_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableCaching
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class AirlineBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineBookingSystemApplication.class, args);
	}

}
