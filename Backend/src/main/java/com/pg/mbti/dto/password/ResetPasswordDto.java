package com.pg.mbti.dto.password;

import com.pg.mbti.util.validator.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
        @NotBlank(message = "Reset token is required")
        String token,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = ValidationPatterns.PASSWORD_REGEX,
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
        )
        String newPassword
) {}
