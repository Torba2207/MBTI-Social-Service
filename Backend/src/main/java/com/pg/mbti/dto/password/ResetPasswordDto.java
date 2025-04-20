package com.pg.mbti.dto.password;

public record ResetPasswordDto(
        String token,
        String newPassword
) {
}