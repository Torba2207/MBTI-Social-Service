package com.pg.mbti.services;

import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.exceptions.QuestionNotFoundException;
import com.pg.mbti.repositories.QuestionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class QuestionsService {

    QuestionsRepository questionsRepository;
    public List<Question> getAllQuestions() {
        return questionsRepository.findAll();
    }

    public Question getQuestionById(UUID id) {
        return questionsRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException("Question not found"));
    }
}
