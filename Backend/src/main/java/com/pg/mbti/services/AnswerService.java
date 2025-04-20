package com.pg.mbti.services;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.entity.questions.Answer;
import com.pg.mbti.entity.questions.AnswerSet;
import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.repositories.AnswerRepository;
import com.pg.mbti.repositories.AnswerSetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerSetRepository answerSetRepository;
    private final QuestionsService questionService;

    @Transactional
    public Answer processUserAnswers(List<UserAnswerDto> userAnswers, MBTIType mbtiType) {
        Answer answer = Answer.builder()
                .mbtiType(mbtiType)
                .answers(new ArrayList<>())
                .build();
        Answer savedAnswer = answerRepository.save(answer);
        List<AnswerSet> answerSets = new ArrayList<>();
        for (UserAnswerDto dto : userAnswers) {
            Question question = questionService.getQuestionById(dto.questionId());
            AnswerSet answerSet = AnswerSet.builder()
                    .answer(answer)
                    .isYes(dto.isYes())
                    .question(question)
                    .build();
            answerSets.add(answerSet);
        }
        answerSetRepository.saveAll(answerSets);
        return savedAnswer;
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }
}