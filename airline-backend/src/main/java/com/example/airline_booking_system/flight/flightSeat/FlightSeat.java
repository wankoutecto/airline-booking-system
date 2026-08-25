package com.example.airline_booking_system.flight.flightSeat;

import com.example.airline_booking_system.aircraft.enums.SeatClass;
import com.example.airline_booking_system.common.entity.BaseEntity;
import com.example.airline_booking_system.flight.Flight;
import com.example.airline_booking_system.flight.enums.FlightSeatStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight_seat")
public class FlightSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatClass seatClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightSeatStatus status;
}
