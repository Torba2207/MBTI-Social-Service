package com.pg.mbti;

import com.pg.mbti.controller.QuestionsController;
import com.pg.mbti.model.questions.Question;
import com.pg.mbti.exception.QuestionNotFoundException;
import com.pg.mbti.service.QuestionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionsControllerTest {

    @Mock
    private QuestionsService questionsService;

    @InjectMocks
    private QuestionsController questionsController;

    @Test
    void getAllQuestionsReturnsAllQuestionsFromService() {
        Question q1 = new Question();
        Question q2 = new Question();
        List<Question> questions = Arrays.asList(q1, q2);

        when(questionsService.getAllQuestions()).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionsController.getAllQuestions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(questions);
        verify(questionsService).getAllQuestions();
    }

    @Test
    void getQuestionByIdReturnsQuestionWhenFound() {
        UUID id = UUID.randomUUID();
        Question question = new Question();

        when(questionsService.getQuestionById(id)).thenReturn(question);

        ResponseEntity<Question> response = questionsController.getQuestionById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(question);
        verify(questionsService).getQuestionById(id);
    }

    @Test
    void getQuestionByIdThrowsExceptionWhenQuestionNotFound() {
        UUID id = UUID.randomUUID();

        when(questionsService.getQuestionById(id)).thenThrow(new QuestionNotFoundException("Question not found"));

        assertThatThrownBy(() -> questionsController.getQuestionById(id))
                .isInstanceOf(QuestionNotFoundException.class)
                .hasMessage("Question not found");

        verify(questionsService).getQuestionById(id);
    }

    @Test
    void createQuestionReturnsCreatedQuestionWithStatus201() {
        Question inputQuestion = new Question();
        Question savedQuestion = new Question();

        when(questionsService.createQuestion(inputQuestion)).thenReturn(savedQuestion);

        ResponseEntity<Question> response = questionsController.createQuestion(inputQuestion);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedQuestion);
        verify(questionsService).createQuestion(inputQuestion);
    }

    @Test
    void getAllQuestionsReturnsEmptyListWhenNoQuestions() {
        List<Question> emptyList = List.of();

        when(questionsService.getAllQuestions()).thenReturn(emptyList);

        ResponseEntity<List<Question>> response = questionsController.getAllQuestions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
        verify(questionsService).getAllQuestions();
    }

    @Test
    void createQuestionWithNullBodyShouldBeDelegatedToService() {
        Question nullQuestion = null;
        Question createdQuestion = new Question();

        when(questionsService.createQuestion(nullQuestion)).thenReturn(createdQuestion);

        ResponseEntity<Question> response = questionsController.createQuestion(nullQuestion);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdQuestion);
        verify(questionsService).createQuestion(nullQuestion);
    }
}