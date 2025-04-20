package com.pg.mbti.components;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.entity.Tag;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.services.AnswerService;
import com.pg.mbti.services.PhotoService;
import com.pg.mbti.services.QuestionsService;
import com.pg.mbti.services.TagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final QuestionsService questionsService;
    private final TagsService tagsService;
    private final AnswerService answerService;
    private final PhotoService photoService;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        initializeQuestions();
        initializeTags();
        initializeAnswers();
        initializeDefaultPhoto();
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

    private void initializeTags() {
        if (!tagsService.getAllTags().isEmpty()) {
            log.info("Tags already exist in database. Skipping initialization.");
            return;
        }

        try {
            log.info("Loading tags from JSON file");
            ClassPathResource resource = new ClassPathResource("data/tags.json");
            try (InputStream inputStream = resource.getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(inputStream);
                List<JsonNode> tagNodes = objectMapper.convertValue(
                        rootNode.get("tags"),
                        new TypeReference<>() {}
                );

                log.info("Found {} tags in JSON file", tagNodes.size());
                tagNodes.forEach(tagNode -> {
                    Tag tag = Tag.builder()
                            .name(tagNode.get("name").asText())
                            .category(tagNode.get("category").asText())
                            .build();
                    tagsService.createTag(tag);
                });
                log.info("Successfully initialized tags from JSON file");
            }
        } catch (IOException e) {
            log.error("Error loading tags from JSON file", e);
        }
    }

    private void initializeAnswers() {
        if (!answerService.getAllAnswers().isEmpty()) {
            log.info("Answers already exist in database. Skipping initialization.");
            return;
        }

        try {
            log.info("Loading MBTI answers from JSON file");
            ClassPathResource resource = new ClassPathResource("data/answers.json");
            try (InputStream inputStream = resource.getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(inputStream);
                JsonNode mbtiAnswers = rootNode.get("answers");

                if (mbtiAnswers == null || mbtiAnswers.isEmpty()) {
                    log.warn("No MBTI answers found in JSON file");
                    return;
                }

                List<Question> allQuestions = questionsService.getAllQuestions();
                if (allQuestions.isEmpty()) {
                    log.error("Cannot initialize answers: no questions found in database");
                    return;
                }

                mbtiAnswers.fieldNames().forEachRemaining(mbtiTypeStr -> {
                    try {
                        MBTIType mbtiType = MBTIType.valueOf(mbtiTypeStr);
                        JsonNode answerSetsNode = mbtiAnswers.get(mbtiTypeStr);

                        if (answerSetsNode.isArray()) {
                            for (JsonNode answerSetNode : answerSetsNode) {
                                List<String> answerSet = objectMapper.convertValue(
                                        answerSetNode,
                                        new TypeReference<>() {}
                                );

                                List<UserAnswerDto> userAnswers = new ArrayList<>();
                                for (int i = 0; i < Math.min(allQuestions.size(), answerSet.size()); i++) {
                                    boolean isYes = "yes".equalsIgnoreCase(answerSet.get(i));
                                    userAnswers.add(new UserAnswerDto(allQuestions.get(i).getId(), isYes));
                                }

                                answerService.processUserAnswers(userAnswers, mbtiType);
                                log.info("Created sample {} answer set", mbtiType);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error processing answers for MBTI type {}: {}", mbtiTypeStr, e.getMessage());
                    }
                });

                log.info("Successfully initialized MBTI answers from JSON file");
            }
        } catch (IOException e) {
            log.error("Error loading MBTI answers from JSON file", e);
        }
    }

    private void initializeDefaultPhoto() {
        try {
            log.info("Initializing default profile picture");
            ClassPathResource resource = new ClassPathResource("static/images/default.png");
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    MultipartFile multipartFile = new MultipartFileImpl(
                            inputStream,
                            resource.contentLength(),
                            "default.png",
                            "image/png"
                    );
                    photoService.uploadPhoto(multipartFile);
                    log.info("Default profile picture uploaded successfully");
                }
            } else {
                log.error("Default profile picture not found in resources");
            }
        } catch (Exception e) {
            log.error("Error uploading default profile picture: {}", e.getMessage());
        }
    }

}