package com.pg.mbti.dto.password;

import com.pg.mbti.util.validator.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents a request to reset a user's password using a reset token.
 * This record contains the token received by the user and the new password.
 */
public record ResetPasswordDto(
        /*
         * The reset token received by the user, typically via email. It cannot be blank.
         */
        @NotBlank(message = "Reset token is required")
        String token,

        /*
         * The new password for the user's account. It cannot be blank, must be at least 8 characters long,
         * and must contain at least one digit, one lowercase letter, one uppercase letter, and one special character.
         */
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = ValidationPatterns.PASSWORD_REGEX,
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
        )
        String newPassword
) {}