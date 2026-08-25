package com.example.airline_booking_system.booking;

import com.example.airline_booking_system.common.entity.BaseEntity;
import com.example.airline_booking_system.flight.flightSeat.FlightSeat;
import com.example.airline_booking_system.user.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingReference;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_seat_id")
    private FlightSeat flightSeat;

}
