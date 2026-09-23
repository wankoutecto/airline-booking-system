package com.example.airline_booking_system.security;


import com.example.airline_booking_system.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex) throws IOException {



        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ApiResponse<Void> apiResponse = new ApiResponse<>(null, "Unauthorized");

        // ObjectMapper.writeValue() converts the object (apiResponse)
        // into JSON and writes it to response.getWriter().
        objectMapper.writeValue(
                response.getWriter(), //where to write
                apiResponse           //what to convert
        );
    }
}