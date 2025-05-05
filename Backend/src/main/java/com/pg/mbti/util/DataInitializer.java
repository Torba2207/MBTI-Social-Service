package com.pg.mbti.util;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.model.Tag;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pg.mbti.model.questions.Question;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.service.AnswerService;
import com.pg.mbti.service.PhotoService;
import com.pg.mbti.service.QuestionsService;
import com.pg.mbti.service.TagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final QuestionsService questionsService;
    private final TagsService tagsService;
    private final AnswerService answerService;
    private final PhotoService photoService;
    private final ObjectMapper objectMapper;

    @Value("${image.default.path}")
    private String defaultProfilePicture;

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

        loadJsonData("data/questions.json", "questions", rootNode ->
                Optional.ofNullable(rootNode.get("questions"))
                        .ifPresent(questionsNode -> {
                            List<String> questionTexts = objectMapper.convertValue(
                                    questionsNode, new TypeReference<>() {});

                            log.info("Found {} questions in JSON file", questionTexts.size());
                            questionTexts.stream()
                                    .map(text -> Question.builder().content(text).build())
                                    .forEach(questionsService::createQuestion);

                            log.info("Successfully initialized questions from JSON file");
                        })
        );
    }

    private void initializeTags() {
        if (!tagsService.getAllTags().isEmpty()) {
            log.info("Tags already exist in database. Skipping initialization.");
            return;
        }

        loadJsonData("data/tags.json", "tags", rootNode ->
                Optional.ofNullable(rootNode.get("tags"))
                        .ifPresent(tagsNode -> {
                            List<JsonNode> tagNodes = objectMapper.convertValue(
                                    tagsNode, new TypeReference<>() {});

                            log.info("Found {} tags in JSON file", tagNodes.size());
                            tagNodes.stream()
                                    .map(node -> Tag.builder()
                                            .name(node.get("name").asText())
                                            .category(node.get("category").asText())
                                            .build())
                                    .forEach(tagsService::createTag);

                            log.info("Successfully initialized tags from JSON file");
                        })
        );
    }

    private void initializeAnswers() {
        if (!answerService.getAllAnswers().isEmpty()) {
            log.info("Answers already exist in database. Skipping initialization.");
            return;
        }

        List<Question> allQuestions = questionsService.getAllQuestions();
        if (allQuestions.isEmpty()) {
            log.error("Cannot initialize answers: no questions found in database");
            return;
        }

        loadJsonData("data/answers.json", "answers", rootNode ->
                Optional.ofNullable(rootNode.get("answers"))
                        .ifPresent(mbtiAnswers -> {
                            final List<Runnable> tasks = new ArrayList<>();

                            mbtiAnswers.fieldNames().forEachRemaining(mbtiTypeStr -> uploadAnswers(allQuestions, mbtiAnswers, mbtiTypeStr, tasks));
                            tasks.forEach(Runnable::run);
                            log.info("Successfully initialized MBTI answers from JSON file");
                        })
        );
    }

    private void uploadAnswers(List<Question> allQuestions, JsonNode mbtiAnswers, String mbtiTypeStr, List<Runnable> tasks) {
        try {
            MBTIType mbtiType = MBTIType.valueOf(mbtiTypeStr);
            JsonNode answerSetsNode = mbtiAnswers.get(mbtiTypeStr);

            if (!answerSetsNode.isArray()) {
                log.error("Invalid answer set format for MBTI type {}: expected an array", mbtiTypeStr);
                return;
            }
            StreamSupport.stream(answerSetsNode.spliterator(), false)
                    .forEach(answerSetNode -> {
                        List<String> answerSet = objectMapper.convertValue(
                                answerSetNode, new TypeReference<>() {});

                        List<UserAnswerDto> userAnswers = IntStream.range(0, Math.min(allQuestions.size(), answerSet.size()))
                                .mapToObj(i -> new UserAnswerDto(
                                        allQuestions.get(i).getId(),
                                        "yes".equalsIgnoreCase(answerSet.get(i))))
                                .toList();

                        tasks.add(() -> {
                            answerService.processUserAnswers(userAnswers, mbtiType);
                            log.info("Created sample {} answer set", mbtiType);
                        });
                    });
        } catch (Exception e) {
            log.error("Error processing answers for MBTI type {}: {}", mbtiTypeStr, e.getMessage());
        }
    }

    private void initializeDefaultPhoto() {
        if (photoService.fileExists(defaultProfilePicture)) {
            log.info("Default profile picture already exists in storage");
            return;
        }

        try {
            ClassPathResource resource = getDefaultPhotoResource();
            if (resource.exists()) {
                MultipartFile multipartFile = createMultipartFile(resource, defaultProfilePicture);
                photoService.uploadPhoto(multipartFile);
                log.info("Default profile picture uploaded successfully");
            } else {
                log.error("Default profile picture not found in resources");
            }
        } catch (Exception e) {
            log.error("Error uploading default profile picture: {}", e.getMessage());
        }
    }

    private ClassPathResource getDefaultPhotoResource() {
        return new ClassPathResource(String.format("static/images/%s", defaultProfilePicture));
    }

    private MultipartFile createMultipartFile(ClassPathResource resource, String filename) throws IOException {
        return new MultipartFileImpl(
                resource.getInputStream(),
                resource.contentLength(),
                filename,
                "image/png"
        );
    }

    private void loadJsonData(String resourcePath, String resourceType, JsonProcessor processor) {
        try {
            log.info("Loading {} from JSON file", resourceType);
            ClassPathResource resource = new ClassPathResource(resourcePath);
            try (InputStream inputStream = resource.getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(inputStream);
                processor.process(rootNode);
            }
        } catch (IOException e) {
            log.error("Error loading {} from JSON file", resourceType, e);
        }
    }

    @FunctionalInterface
    private interface JsonProcessor {
        void process(JsonNode rootNode) throws IOException;
    }
}