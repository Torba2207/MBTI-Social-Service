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

    public Map<String, Object> nextStep(List<UserAnswerDto> answers) {
        boolean[] answerValues = new boolean[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            answerValues[i] = answers.get(i).isYes();
        }

        MbtiPredictionRequest request = new MbtiPredictionRequest(answerValues);
        return restTemplate.postForObject(mbtiApiUrl + "/api/mbti/next", request, Map.class);
    }

}