package com.pg.mbti.service;

import com.pg.mbti.dto.prediction.MbtiPredictionRequest;
import com.pg.mbti.dto.prediction.MbtiTrainingRequest;
import com.pg.mbti.dto.UserAnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.List;
import java.util.Map;

/**
 * Service class for interacting with an external MBTI prediction Python API.
 * Handles model training and next step prediction requests.
 */
@Service
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class MbtiPredictionService {

    private final RestTemplate restTemplate;

    @Value("${mbti.python.api.url}")
    private String mbtiApiUrl;

    /**
     * Sends a request to the Python API to train the MBTI prediction model.
     *
     * @param depth The depth parameter for the training algorithm.
     * @return A success message from the API.
     * @throws ResponseStatusException If the training request fails or returns an unexpected response.
     */
    public String trainModel(int depth) {
        String url = mbtiApiUrl + "/api/mbti/train";
        log.info("Sending training request to MBTI Python API with depth: {}", depth); // Log training request
        MbtiTrainingRequest request = new MbtiTrainingRequest(depth);
        Map<String, String> response = restTemplate.postForObject(url, request, Map.class);

        if (response != null && "success".equals(response.get("status"))) {
            log.info("MBTI model training successful. Message: {}", response.get("message")); // Log successful training
            return response.get("message");
        } else {
            log.error("Failed to train MBTI prediction model. API response: {}", response); // Log training failure
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to train MBTI prediction model");
        }
    }

    /**
     * Sends user answers to the Python API to get the next step or prediction.
     *
     * @param answers A list of {@link UserAnswerDto} containing the user's answers.
     * @return A map containing the prediction result from the API.
     */
    public Map<String, Object> nextStep(List<UserAnswerDto> answers) {
        log.info("Sending next step prediction request to MBTI Python API with {} answers.", answers.size()); // Log prediction request
        boolean[] answerValues = new boolean[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            answerValues[i] = answers.get(i).isYes();
        }

        MbtiPredictionRequest request = new MbtiPredictionRequest(answerValues);
        Map<String, Object> response = restTemplate.postForObject(mbtiApiUrl + "/api/mbti/next", request, Map.class);
        log.debug("Received prediction response: {}", response); // Log prediction response
        return response;
    }

}