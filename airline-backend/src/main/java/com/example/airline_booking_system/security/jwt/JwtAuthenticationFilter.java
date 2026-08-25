package com.example.airline_booking_system.security.jwt;


import com.example.airline_booking_system.common.exception.ResourceNotFoundException;
import com.example.airline_booking_system.security.CustomUserDetails;
import com.example.airline_booking_system.user.User;
import com.example.airline_booking_system.user.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            System.out.println("no header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Long tokenUserId = jwtService.extractUserId(token);
            if(tokenUserId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = userService.findUserById(tokenUserId);
                CustomUserDetails userDetails = new CustomUserDetails(user);
                if(jwtService.isTokenValid(token, userDetails.getId())){
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken
                                    (userDetails, null, userDetails.getAuthorities()));
                }
            }
        } catch (JwtException | ResourceNotFoundException ex) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
