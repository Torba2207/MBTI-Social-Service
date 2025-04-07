package com.pg.mbti.dto;

public record LoginRequestDto(
        String usernameOrEmail,
        String password
) {
}
