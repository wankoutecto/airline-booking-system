package com.example.airline_booking_system.security.jwt;


import com.example.airline_booking_system.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    private Key key;
    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        key = Keys.hmacShaKeyFor(bytes);
    }
    public String generateToken(CustomUserDetails userDetails){
        Map<String, List<String>> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        String subject = String.valueOf(userDetails.getId());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key)
                .compact();
    }
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractSubject(String token){
        return extractClaims(token).getSubject();
    }
    public List<String> extractRoles(String token){
        List<?> roles = extractClaims(token).get("roles", List.class);
        if(roles == null) return List.of();

        return  roles.stream().map(Object::toString).toList();
    }
    public Date extractExpiration(String token){

        return extractClaims(token).getExpiration();
    }
    public Long extractUserId(String token){

        return Long.parseLong(extractClaims(token).getSubject());
    }
    public boolean isTokenValid(String token,  Long userId){

        Claims claims = extractClaims(token);
        boolean isNotExpire = claims.getExpiration().after(new Date());

        return  Objects.equals(claims.getSubject(), String.valueOf(userId)) && isNotExpire;
    }

}
