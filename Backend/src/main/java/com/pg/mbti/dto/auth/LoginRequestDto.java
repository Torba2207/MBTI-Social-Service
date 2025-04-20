package com.pg.mbti.dto.auth;

public record LoginRequestDto(
        String usernameOrEmail,
        String password
) {
}
