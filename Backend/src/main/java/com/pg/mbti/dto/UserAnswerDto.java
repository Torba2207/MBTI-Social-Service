package com.pg.mbti.dto;

import java.util.UUID;

/**
 * Represents a user's answer to a specific question.
 * This record stores the ID of the question and the boolean value of the answer.
 */
public record UserAnswerDto(
        /*
         * The unique identifier of the question.
         */
        UUID questionId,
        /*
         * A boolean value indicating the user's answer to the question (e.g., true for "Yes", false for "No").
         */
        boolean isYes
) {
}