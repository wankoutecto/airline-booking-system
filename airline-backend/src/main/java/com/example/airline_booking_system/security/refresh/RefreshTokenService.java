package com.example.airline_booking_system.security.refresh;

import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import com.example.airline_booking_system.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private Duration refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(generateToken());
        refreshToken.setRevoked(false);
        refreshToken.setExpiresAt(LocalDateTime.now().plus(refreshTokenExpiration));
        return refreshTokenRepository.save(refreshToken);
    }

    private String generateToken(){
        byte[] randomBytes = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(randomBytes);
    }


    private RefreshToken verifyRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (refreshToken.isRevoked() || LocalDateTime.now().isAfter(refreshToken.getExpiresAt())) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
        return refreshToken;
    }
    private void revokeRefreshToken(RefreshToken oldRefreshToken){
        oldRefreshToken.setRevoked(true);
        refreshTokenRepository.save(oldRefreshToken);
    }
    private void revokeAllRefreshTokens(List<RefreshToken> refreshTokenList){
        for(RefreshToken refreshToken: refreshTokenList){
            refreshToken.setRevoked(true);
        }
        refreshTokenRepository.saveAll(refreshTokenList);
    }

    private RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken){
        revokeRefreshToken(oldRefreshToken);
        return createRefreshToken(oldRefreshToken.getUser());
    }
    @Transactional
    public RefreshToken refresh(String token){
        RefreshToken oldRefreshToken = verifyRefreshToken(token);
        return rotateRefreshToken(oldRefreshToken);
    }
    @Transactional
    public void logout(String token){
        RefreshToken oldRefreshToken = verifyRefreshToken(token);
        List<RefreshToken> refreshTokenList = refreshTokenRepository
                .findAllByUser(oldRefreshToken.getUser());
        revokeAllRefreshTokens(refreshTokenList);
    }


}
