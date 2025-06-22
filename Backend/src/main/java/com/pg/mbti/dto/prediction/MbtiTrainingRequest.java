package com.pg.mbti.dto.prediction;

/**
 * Represents a request to initiate MBTI model training.
 * This record specifies the depth parameter for the training process.
 */
public record MbtiTrainingRequest(
        /*
         * The depth parameter used for training the MBTI prediction model.
         */
        int depth
) {
}