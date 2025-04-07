package com.pg.mbti.dto;

import java.util.UUID;

public record UserAnswerDto(
        UUID questionId,
        boolean isYes
) {
}
