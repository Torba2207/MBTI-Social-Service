package com.pg.mbti.dto.prediction;

/**
 * Represents a request for an MBTI prediction based on a series of answers.
 * This record contains an array of boolean values corresponding to the answers given in a test.
 */
public record MbtiPredictionRequest(
        /*
         * An array of boolean values representing the answers to the MBTI prediction questions.
         * Each boolean corresponds to a specific question's answer.
         */
        boolean[] answers
) {
}