package com.pg.mbti.dto;

import lombok.Builder;

@Builder
public record UpdatePasswordDto(
        String oldPassword,
        String newPassword
) {
}