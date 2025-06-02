package com.pg.mbti.service;

import com.pg.mbti.model.questions.Question;
import com.pg.mbti.exception.QuestionNotFoundException;
import com.pg.mbti.repository.QuestionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing questions related to MBTI assessments.
 */
@AllArgsConstructor
@Service
@Slf4j // Enable logging for this class
public class QuestionsService {

    private final QuestionsRepository questionsRepository;

    /**
     * Retrieves all questions from the repository.
     *
     * @return A list of all {@link Question} entities.
     */
    public List<Question> getAllQuestions() {
        log.debug("Fetching all questions."); // Log fetching all questions
        return questionsRepository.findAll();
    }

    /**
     * Retrieves a question by its unique identifier.
     *
     * @param id The UUID of the question to retrieve.
     * @return The {@link Question} entity if found.
     * @throws QuestionNotFoundException If no question is found with the given ID.
     */
    public Question getQuestionById(UUID id) {
        log.debug("Attempting to retrieve question with ID: {}", id); // Log question retrieval attempt
        return questionsRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Question not found with ID: {}", id); // Log question not found
                    return new QuestionNotFoundException("Question not found");
                });
    }

    /**
     * Creates a new question in the repository.
     *
     * @param question The {@link Question} entity to create.
     * @return The saved {@link Question} entity.
     */
    public Question createQuestion(Question question) {
        log.info("Creating new question: {}", question.getContent()); // Log question creation
        return questionsRepository.save(question);
    }
}