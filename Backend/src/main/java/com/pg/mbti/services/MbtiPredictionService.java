package com.pg.mbti.services;

import com.pg.mbti.dto.prediction.MbtiPredictionRequest;
import com.pg.mbti.dto.prediction.MbtiTrainingRequest;
import com.pg.mbti.dto.prediction.MbtiPredictionResponse;
import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.enums.MBTIType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MbtiPredictionService {

    private final RestTemplate restTemplate;

    @Value("${mbti.python.api.url}")
    private String mbtiApiUrl;

    public String trainModel(int depth) {
        String url = mbtiApiUrl + "/api/mbti/train";

        MbtiTrainingRequest request = new MbtiTrainingRequest(depth);
        Map<String, String> response = restTemplate.postForObject(url, request, Map.class);

        if (response != null && "success".equals(response.get("status"))) {
            return response.get("message");
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to train MBTI prediction model");
        }
    }

    public List<String> getQuestions() {
        String url = mbtiApiUrl + "/api/mbti/questions";
        Map<String, List<String>> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("questions")) {
            return response.get("questions");
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve MBTI questions");
        }
    }

    public MBTIType predictPersonality(List<UserAnswerDto> answers) {
        String url = mbtiApiUrl + "/api/mbti/predict";

        // Convert UserAnswerDto list to list of boolean values
        boolean[] answerValues = new boolean[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            answerValues[i] = answers.get(i).isYes();
        }

        MbtiPredictionRequest request = new MbtiPredictionRequest(answerValues);
        MbtiPredictionResponse response = restTemplate.postForObject(url, request, MbtiPredictionResponse.class);

        if (response != null && response.prediction() != null) {
            return MBTIType.valueOf(response.prediction());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to predict MBTI personality type");
        }
    }
}