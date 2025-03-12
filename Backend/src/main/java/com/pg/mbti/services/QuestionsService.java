package com.pg.mbti.services;

import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.repositories.QuestionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class QuestionsService {
    QuestionsRepository questionsRepository;
    public List<Question> getAllQuestions() {
        return questionsRepository.findAll();
    }

}
