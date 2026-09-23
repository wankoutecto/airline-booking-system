package com.example.airline_booking_system.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultRedisScript<Long> rateLimitScript;
    @Value("${app.count-limit}")
    private Long countLimit;
    @Value("${app.window-size}")
    private int windowSize;

    public RateLimitFilter(RedisTemplate<String, String> redisTemplate, DefaultRedisScript<Long> rateLimitScript) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = rateLimitScript;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        long now = System.currentTimeMillis();
        long windowStart = now - (windowSize * 60_000L);
        String requestId = UUID.randomUUID().toString();
        String key = "rate_limit:" + ip;
        long ttlSeconds = windowSize * 60L;

        long status = redisTemplate.execute(rateLimitScript, List.of(key),
                        String.valueOf(now),
                        String.valueOf(windowStart),
                        String.valueOf(ttlSeconds),
                        String.valueOf(countLimit),
                        requestId);

        if(status == 0){
            response.setStatus(429);
            return;
        }

        filterChain.doFilter(request, response);
    }
}




