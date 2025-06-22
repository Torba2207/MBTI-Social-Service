package com.pg.mbti.service;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.model.questions.Answer;
import com.pg.mbti.model.questions.AnswerSet;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.repository.AnswerRepository;
import com.pg.mbti.repository.AnswerSetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for processing user answers to MBTI questions and managing answer sets.
 */
@Service
@AllArgsConstructor
@Slf4j // Enable logging for this class
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerSetRepository answerSetRepository;
    private final QuestionsService questionService;

    /**
     * Processes a list of user answers, associates them with an MBTI type,
     * and saves them as a new answer set.
     *
     * @param userAnswers A list of {@link UserAnswerDto} containing the user's answers.
     * @param mbtiType The predicted {@link MBTIType} for these answers.
     * @return The saved {@link Answer} entity.
     */
    @Transactional
    public Answer processUserAnswers(List<UserAnswerDto> userAnswers, MBTIType mbtiType) {
        log.info("Processing user answers for MBTI type: {}", mbtiType); // Log the start of processing answers
        Answer answer = answerRepository.save(Answer.builder()
                .mbtiType(mbtiType)
                .answers(new ArrayList<>())
                .build());
        log.debug("New Answer entity created with ID: {} and MBTI type: {}", answer.getId(), mbtiType); // Log Answer entity creation

        List<AnswerSet> answerSets = userAnswers.stream()
                .map(dto -> {
                    log.debug("Mapping UserAnswerDto for question ID: {} with answer: {}", dto.questionId(), dto.isYes()); // Log mapping each answer DTO
                    return AnswerSet.builder()
                            .answer(answer)
                            .isYes(dto.isYes())
                            .question(questionService.getQuestionById(dto.questionId()))
                            .build();
                })
                .collect(Collectors.toList());

        answerSetRepository.saveAll(answerSets);
        log.info("Successfully saved {} answer sets for Answer ID: {}", answerSets.size(), answer.getId()); // Log successful saving of answer sets
        return answer;
    }

    /**
     * Retrieves all stored answers.
     *
     * @return A list of all {@link Answer} entities.
     */
    public List<Answer> getAllAnswers() {
        log.debug("Fetching all answers from the repository."); // Log fetching all answers
        return answerRepository.findAll();
    }
}