package com.pg.mbti.util;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for generating, storing, and retrieving secure tokens using Redis.
 * Useful for purposes like email confirmation or password reset.
 */
@Component
@AllArgsConstructor
@Slf4j // Enable logging for this class
public class SecureTokenUtil {
    private final RedisTemplate<String, String> redisTemplate; // RedisTemplate for interacting with Redis

    /**
     * Generates a unique secure token and stores it in Redis with an associated value and expiration duration.
     *
     * @param valueToStore The string value to associate with the token (e.g., user ID, email).
     * @param duration The duration for which the token will be valid.
     * @param timeUnit The time unit for the duration (e.g., {@link TimeUnit#DAYS}).
     * @return An {@link Optional} containing the generated token string if successful, otherwise empty.
     */
    public Optional<String> generateToken(String valueToStore, int duration, TimeUnit timeUnit) {
        String token = UUID.randomUUID().toString(); // Generate a random UUID as the token
        try {
            log.debug("Generating token for value: {} with duration {} {}", valueToStore, duration, timeUnit); // Log token generation attempt
            redisTemplate.opsForValue().set(token, valueToStore, duration, timeUnit); // Store token-value pair in Redis with expiration
            log.info("Token generated successfully: {}", token); // Log successful token generation
        } catch (Exception e) {
            log.error("Failed to generate token for value {}: {}", valueToStore, e.getMessage()); // Log token generation failure
            return Optional.empty(); // Return empty optional if an error occurs
        }
        return Optional.of(token); // Return the generated token wrapped in an Optional
    }

    /**
     * Retrieves the value associated with a given token from Redis.
     *
     * @param token The secure token to look up.
     * @return An {@link Optional} containing the stored value if the token exists, otherwise empty.
     */
    public Optional<String> getValue(String token) {
        log.debug("Attempting to retrieve value for token: {}", token); // Log value retrieval attempt
        String value = redisTemplate.opsForValue().get(token);
        if (value != null) {
            log.debug("Value retrieved for token {}: {}", token, value); // Log successful value retrieval
        } else {
            log.warn("No value found for token: {}", token); // Log no value found
        }
        return Optional.ofNullable(value); // Return the retrieved value as an Optional
    }

    /**
     * Deletes a token and its associated value from Redis.
     *
     * @param token The token to delete.
     */
    public void deleteValue(String token) {
        log.debug("Attempting to delete token: {}", token); // Log token deletion attempt
        Boolean deleted = redisTemplate.delete(token); // Delete the token from Redis
        if (deleted) {
            log.info("Token {} deleted successfully.", token); // Log successful deletion
        } else {
            log.warn("Token {} not found or already deleted.", token); // Log if token was not found for deletion
        }
    }
}