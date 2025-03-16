package com.pg.mbti.dto;

import lombok.Builder;

@Builder
public record EmailContextDto(
        String recipient,
        String subject,
        String message
) {
}
