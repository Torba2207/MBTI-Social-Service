package com.pg.mbti.dto.auth;

/**
 * Represents the response returned after a successful user registration.
 * This record typically contains the username and email of the newly registered user.
 */
public record RegistrationResponseDto(
        /*
         * The username of the newly registered user.
         */
        String username,
        /*
         * The email of the newly registered user.
         */
        String email
) {
}