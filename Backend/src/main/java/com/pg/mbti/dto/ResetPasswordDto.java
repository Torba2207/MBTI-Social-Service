package com.pg.mbti.dto;

public record ResetPasswordDto(
        String token,
        String newPassword
) {
}