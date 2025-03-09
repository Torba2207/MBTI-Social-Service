package com.pg.mbti.controllers;

import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.services.QuestionsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class QuestionsController {
    QuestionsService questionsService;

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionsService.getAllQuestions();
    }
}
