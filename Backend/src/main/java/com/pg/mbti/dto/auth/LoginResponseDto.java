package com.pg.mbti.dto.auth;

/**
 * Represents the response returned after a successful user login.
 * This record typically contains a confirmation message or token.
 */
public record LoginResponseDto(
        /*
         * A string indicating the response of the login operation,
         * e.g., a success message or an authentication token.
         */
        String response
) {
}