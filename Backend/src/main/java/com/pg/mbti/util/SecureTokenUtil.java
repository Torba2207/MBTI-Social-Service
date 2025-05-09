package com.pg.mbti.util;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class SecureTokenUtil {
    private final RedisTemplate<String, String> redisTemplate;

    public Optional<String> generateToken(String valueToStore, int duration, TimeUnit timeUnit) {
        String token = UUID.randomUUID().toString();
        try {
            redisTemplate.opsForValue().set(token, valueToStore, duration, timeUnit);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public Optional<String> getValue(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }

    public void deleteValue(String token) {
        redisTemplate.delete(token);
    }
}
