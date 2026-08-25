package com.example.airline_booking_system.auth;

import com.example.airline_booking_system.auth.dto.LoginRequest;
import com.example.airline_booking_system.auth.dto.LoginResponse;
import com.example.airline_booking_system.auth.dto.RegisterRequest;
import com.example.airline_booking_system.security.CustomUserDetails;
import com.example.airline_booking_system.security.jwt.JwtService;
import com.example.airline_booking_system.security.refresh.RefreshToken;
import com.example.airline_booking_system.security.refresh.RefreshTokenService;
import com.example.airline_booking_system.user.User;
import com.example.airline_booking_system.user.UserRepository;
import com.example.airline_booking_system.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }


    public void register(RegisterRequest request) {
        userService.createUser(request);
    }
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userService.findUserById(userDetails.getId());

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    public LoginResponse refresh(String token){
        RefreshToken newRefreshToken = refreshTokenService.refresh(token);
        CustomUserDetails customUserDetails = new CustomUserDetails(newRefreshToken.getUser());
        String accessToken = jwtService.generateToken(customUserDetails);
        return new LoginResponse(accessToken, newRefreshToken.getToken());
    }
    public void logout(String token){
        refreshTokenService.logout(token);
    }
}
