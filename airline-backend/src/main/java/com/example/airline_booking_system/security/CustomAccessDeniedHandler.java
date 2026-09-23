package com.example.airline_booking_system.security;

import com.example.airline_booking_system.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ApiResponse<Void> apiResponse =
                new ApiResponse<>(null, "Access denied");

        // ObjectMapper.writeValue() converts the object (apiResponse)
        // into JSON and writes it to response.getWriter().
        objectMapper.writeValue(
                response.getWriter(),
                apiResponse
        );
    }
}
