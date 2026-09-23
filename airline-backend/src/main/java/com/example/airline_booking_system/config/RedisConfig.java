package com.example.airline_booking_system.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                )
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
    }

    @Bean
    public DefaultRedisScript<Long> rateLimitScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();

        script.setLocation(
                new ClassPathResource("redis/rate-limit.lua")
        );

        script.setResultType(Long.class);

        return script;
    }
}
