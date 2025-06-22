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

/**
 * Component responsible for initializing application data (questions, tags, answers, default photo)
 * when the application starts. It implements {@link CommandLineRunner} to run after Spring context is loaded.
 */
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

    /**
     * Callback used to run the data initialization logic after the application context is loaded.
     *
     * @param args Command line arguments (not used in this implementation).
     */
    @Override
    public void run(String... args) {
        log.info("Starting data initialization..."); // Log the start of data initialization
        initializeQuestions();
        initializeTags();
        initializeAnswers();
        initializeDefaultPhoto();
        log.info("Data initialization completed."); // Log the completion of data initialization
    }

    /**
     * Initializes questions from a JSON file if no questions already exist in the database.
     */
    private void initializeQuestions() {
        if (!questionsService.getAllQuestions().isEmpty()) {
            log.info("Questions already exist in database. Skipping initialization."); // Log if questions already exist
            return;
        }

        loadJsonData("data/questions.json", "questions", rootNode ->
                Optional.ofNullable(rootNode.get("questions"))
                        .ifPresent(questionsNode -> {
                            List<String> questionTexts = objectMapper.convertValue(
                                    questionsNode, new TypeReference<>() {});

                            log.info("Found {} questions in JSON file", questionTexts.size()); // Log number of questions found
                            questionTexts.stream()
                                    .map(text -> Question.builder().content(text).build())
                                    .forEach(questionsService::createQuestion);

                            log.info("Successfully initialized questions from JSON file"); // Log successful question initialization
                        })
        );
    }

    /**
     * Initializes tags from a JSON file if no tags already exist in the database.
     */
    private void initializeTags() {
        if (!tagsService.getAllTags().isEmpty()) {
            log.info("Tags already exist in database. Skipping initialization."); // Log if tags already exist
            return;
        }

        loadJsonData("data/tags.json", "tags", rootNode ->
                Optional.ofNullable(rootNode.get("tags"))
                        .ifPresent(tagsNode -> {
                            List<JsonNode> tagNodes = objectMapper.convertValue(
                                    tagsNode, new TypeReference<>() {});

                            log.info("Found {} tags in JSON file", tagNodes.size()); // Log number of tags found
                            tagNodes.stream()
                                    .map(node -> Tag.builder()
                                            .name(node.get("name").asText())
                                            .category(node.get("category").asText())
                                            .build())
                                    .forEach(tagsService::createTag);

                            log.info("Successfully initialized tags from JSON file"); // Log successful tag initialization
                        })
        );
    }

    /**
     * Initializes sample answers for MBTI types from a JSON file if no answers already exist.
     * It relies on questions being present in the database.
     */
    private void initializeAnswers() {
        if (!answerService.getAllAnswers().isEmpty()) {
            log.info("Answers already exist in database. Skipping initialization."); // Log if answers already exist
            return;
        }

        List<Question> allQuestions = questionsService.getAllQuestions();
        if (allQuestions.isEmpty()) {
            log.error("Cannot initialize answers: no questions found in database"); // Log error if no questions are found
            return;
        }

        loadJsonData("data/answers.json", "answers", rootNode ->
                Optional.ofNullable(rootNode.get("answers"))
                        .ifPresent(mbtiAnswers -> {
                            final List<Runnable> tasks = new ArrayList<>();

                            mbtiAnswers.fieldNames().forEachRemaining(mbtiTypeStr -> uploadAnswers(allQuestions, mbtiAnswers, mbtiTypeStr, tasks));
                            tasks.forEach(Runnable::run);
                            log.info("Successfully initialized MBTI answers from JSON file"); // Log successful answer initialization
                        })
        );
    }

    /**
     * Helper method to process and prepare answer sets for a specific MBTI type.
     *
     * @param allQuestions A list of all available questions.
     * @param mbtiAnswers The JSON node containing answers for various MBTI types.
     * @param mbtiTypeStr The string representation of the current MBTI type.
     * @param tasks A list of {@link Runnable} tasks to be executed (for processing answers).
     */
    private void uploadAnswers(List<Question> allQuestions, JsonNode mbtiAnswers, String mbtiTypeStr, List<Runnable> tasks) {
        try {
            MBTIType mbtiType = MBTIType.valueOf(mbtiTypeStr);
            JsonNode answerSetsNode = mbtiAnswers.get(mbtiTypeStr);

            if (!answerSetsNode.isArray()) {
                log.error("Invalid answer set format for MBTI type {}: expected an array", mbtiTypeStr); // Log invalid answer set format
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
                            log.info("Created sample {} answer set", mbtiType); // Log creation of sample answer set
                        });
                    });
        } catch (Exception e) {
            log.error("Error processing answers for MBTI type {}: {}", mbtiTypeStr, e.getMessage()); // Log errors during answer processing
        }
    }

    /**
     * Initializes the default profile photo in storage if it doesn't already exist.
     */
    private void initializeDefaultPhoto() {
        if (photoService.fileExists(defaultProfilePicture)) {
            log.info("Default profile picture already exists in storage"); // Log if default photo already exists
            return;
        }

        try {
            ClassPathResource resource = getDefaultPhotoResource();
            if (resource.exists()) {
                MultipartFile multipartFile = createMultipartFile(resource, defaultProfilePicture);
                photoService.uploadPhoto(multipartFile);
                log.info("Default profile picture uploaded successfully"); // Log successful default photo upload
            } else {
                log.error("Default profile picture not found in resources at path: static/images/{}", defaultProfilePicture); // Log if default photo resource is missing
            }
        } catch (Exception e) {
            log.error("Error uploading default profile picture: {}", e.getMessage()); // Log errors during default photo upload
        }
    }

    /**
     * Retrieves the {@link ClassPathResource} for the default profile photo.
     *
     * @return The {@link ClassPathResource} for the default photo.
     */
    private ClassPathResource getDefaultPhotoResource() {
        return new ClassPathResource(String.format("static/images/%s", defaultProfilePicture));
    }

    /**
     * Creates a {@link MultipartFile} instance from a {@link ClassPathResource}.
     *
     * @param resource The {@link ClassPathResource} containing the file data.
     * @param filename The desired filename for the multipart file.
     * @return A {@link MultipartFile} implementation.
     * @throws IOException If an I/O error occurs while reading the resource.
     */
    private MultipartFile createMultipartFile(ClassPathResource resource, String filename) throws IOException {
        return new MultipartFileImpl(
                resource.getInputStream(),
                resource.contentLength(),
                filename,
                "image/png" // Assuming default profile picture is PNG
        );
    }

    /**
     * Loads JSON data from a specified resource path and processes it using a {@link JsonProcessor}.
     *
     * @param resourcePath The path to the JSON file within the classpath.
     * @param resourceType A descriptive name for the type of resource being loaded (e.g., "questions").
     * @param processor The functional interface to process the loaded {@link JsonNode}.
     */
    private void loadJsonData(String resourcePath, String resourceType, JsonProcessor processor) {
        try {
            log.info("Loading {} from JSON file: {}", resourceType, resourcePath); // Log loading JSON data
            ClassPathResource resource = new ClassPathResource(resourcePath);
            try (InputStream inputStream = resource.getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(inputStream);
                processor.process(rootNode);
            }
        } catch (IOException e) {
            log.error("Error loading {} from JSON file: {}", resourceType, resourcePath, e); // Log error loading JSON data
        }
    }

    /**
     * Functional interface for processing a {@link JsonNode}.
     */
    @FunctionalInterface
    private interface JsonProcessor {
        void process(JsonNode rootNode) throws IOException;
    }
}