package com.example.airline_booking_system.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {
}
