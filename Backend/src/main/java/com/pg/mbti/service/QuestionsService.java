package com.pg.mbti.service;

import com.pg.mbti.model.questions.Question;
import com.pg.mbti.exception.QuestionNotFoundException;
import com.pg.mbti.repository.QuestionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class QuestionsService {

    private final QuestionsRepository questionsRepository;

    public List<Question> getAllQuestions() {
        return questionsRepository.findAll();
    }

    public Question getQuestionById(UUID id) {
        return questionsRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));
    }

    public Question createQuestion(Question question) {
        return questionsRepository.save(question);
    }
}
