package com.pg.mbti.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SecureTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public String generateToken(String valueToStore, int duration, TimeUnit timeUnit) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token, valueToStore, duration, timeUnit);
        return token;
    }

    public Optional<String> getValue(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }

    public void deleteValue(String token) {
        redisTemplate.delete(token);
    }
}
