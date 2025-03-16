package com.pg.mbti.dto;

public record EmailContextDto(
        String recipient,
        String subject,
        String message
) {
}
