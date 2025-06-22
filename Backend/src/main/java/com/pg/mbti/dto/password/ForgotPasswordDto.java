package com.pg.mbti.dto.password;

/**
 * Represents a request to initiate the forgot password process.
 * This record typically contains the email of the user who forgot their password.
 */
public record ForgotPasswordDto(
        /*
         * The email address associated with the account for which the password needs to be reset.
         */
        String email
) {
}