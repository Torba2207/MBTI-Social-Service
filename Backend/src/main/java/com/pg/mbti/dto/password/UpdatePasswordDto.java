package com.pg.mbti.dto.password;

import lombok.Builder;

@Builder
public record UpdatePasswordDto(
        String oldPassword,
        String newPassword
) {
}