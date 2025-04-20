package com.pg.mbti.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.services.QuestionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final QuestionsService questionsService;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        initializeQuestions();
    }

    private void initializeQuestions() {
        if (!questionsService.getAllQuestions().isEmpty()) {
            log.info("Questions already exist in database. Skipping initialization.");
            return;
        }

        try {
            log.info("Loading questions from JSON file");
            ClassPathResource resource = new ClassPathResource("data/questions.json");
            try (InputStream inputStream = resource.getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(inputStream);
                List<String> questionTexts = objectMapper.convertValue(
                        rootNode.get("questions"),
                        new TypeReference<>() {}
                );

                log.info("Found {} questions in JSON file", questionTexts.size());
                questionTexts.forEach(text -> {
                    Question question = Question.builder()
                            .content(text)
                            .build();
                    questionsService.createQuestion(question);
                });
                log.info("Successfully initialized questions from JSON file");
            }
        } catch (IOException e) {
            log.error("Error loading questions from JSON file", e);
        }
    }
}