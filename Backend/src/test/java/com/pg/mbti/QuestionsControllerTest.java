package com.pg.mbti;

import com.pg.mbti.controllers.QuestionsController;
import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.entity.questions.Answer;
import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.exceptions.QuestionNotFoundException;
import com.pg.mbti.services.AnswerService;
import com.pg.mbti.services.QuestionsService;
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

    @Mock
    private AnswerService answerService;

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
    void submitAnswersProcessesUserAnswersSuccessfully() {
        UUID answerId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        List<UserAnswerDto> answers = List.of(new UserAnswerDto(questionId, true));
        MBTIType mbtiType = MBTIType.ENFJ;

        Answer answer = Answer.builder().id(answerId).build();

        when(answerService.processUserAnswers(answers, mbtiType)).thenReturn(answer);

        ResponseEntity<String> response = questionsController.submitAnswers(answers, mbtiType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Answers submitted successfully with ID: " + answerId);
        verify(answerService).processUserAnswers(answers, mbtiType);
    }

    @Test
    void submitAnswersHandlesEmptyAnswersList() {
        UUID answerId = UUID.randomUUID();
        List<UserAnswerDto> emptyAnswers = List.of();
        MBTIType mbtiType = MBTIType.INTJ;

        Answer answer = Answer.builder().id(answerId).build();
        when(answerService.processUserAnswers(emptyAnswers, mbtiType)).thenReturn(answer);

        ResponseEntity<String> response = questionsController.submitAnswers(emptyAnswers, mbtiType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Answers submitted successfully with ID: " + answerId);
    }
}