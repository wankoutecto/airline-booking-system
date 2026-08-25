package com.example.airline_booking_system.aircraft.aircraftSeat;

import com.example.airline_booking_system.aircraft.Aircraft;
import com.example.airline_booking_system.aircraft.enums.SeatClass;
import com.example.airline_booking_system.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "aircraft_seat")
public class AircraftSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
}
