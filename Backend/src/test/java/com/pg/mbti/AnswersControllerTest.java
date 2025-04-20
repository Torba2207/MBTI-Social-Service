package com.pg.mbti;

import com.pg.mbti.controllers.AnswerController;
import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.entity.questions.Answer;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.services.AnswerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswersControllerTest {
    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    @Test
    void submitAnswersProcessesUserAnswersSuccessfully() {
        UUID answerId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        List<UserAnswerDto> answers = List.of(new UserAnswerDto(questionId, true));
        MBTIType mbtiType = MBTIType.ENFJ;

        Answer answer = Answer.builder().id(answerId).build();

        when(answerService.processUserAnswers(answers, mbtiType)).thenReturn(answer);

        ResponseEntity<String> response = answerController.submitAnswers(answers, mbtiType);

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

        ResponseEntity<String> response = answerController.submitAnswers(emptyAnswers, mbtiType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Answers submitted successfully with ID: " + answerId);
    }
}
