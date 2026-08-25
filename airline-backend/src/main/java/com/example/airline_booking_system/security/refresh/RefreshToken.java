package com.example.airline_booking_system.security.refresh;

import com.example.airline_booking_system.common.entity.BaseEntity;
import com.example.airline_booking_system.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private boolean revoked;
    private LocalDateTime expiresAt;

    @Version
    private Long version;

    //FetchType.Lazy load user's data only when business operations needs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
