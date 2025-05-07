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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerSetRepository answerSetRepository;
    private final QuestionsService questionService;

    @Transactional
    public Answer processUserAnswers(List<UserAnswerDto> userAnswers, MBTIType mbtiType) {
        Answer answer = answerRepository.save(Answer.builder()
                .mbtiType(mbtiType)
                .answers(new ArrayList<>())
                .build());

        List<AnswerSet> answerSets = userAnswers.stream()
                .map(dto -> AnswerSet.builder()
                        .answer(answer)
                        .isYes(dto.isYes())
                        .question(questionService.getQuestionById(dto.questionId()))
                        .build())
                .collect(Collectors.toList());

        answerSetRepository.saveAll(answerSets);
        return answer;
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }
}