package com.pg.mbti.dto.auth;

/**
 * Represents a request for user login.
 * This record contains the credentials needed to authenticate a user.
 */
public record LoginRequestDto(
        /*
          The username or email of the user attempting to log in.
         */
        String usernameOrEmail,
        /*
         * The password of the user attempting to log in.
         */
        String password
) {
}