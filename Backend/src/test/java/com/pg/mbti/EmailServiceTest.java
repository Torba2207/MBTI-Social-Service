package com.pg.mbti;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendMailSuccessfully() {
        EmailContextDto emailContext = new EmailContextDto(
                "test@example.com",
                "Test Subject",
                "Test Message");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendMail(emailContext);

        verify(emailSender).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertThat(capturedMessage.getFrom()).isEqualTo("mbtiservice2@gmail.com");
        assertThat(capturedMessage.getTo()).containsExactly("test@example.com");
        assertThat(capturedMessage.getSubject()).isEqualTo("Test Subject");
        assertThat(capturedMessage.getText()).isEqualTo("Test Message");
    }

    @Test
    void throwsEmailSendingFailedExceptionWhenSendingFails() {
        EmailContextDto emailContext = new EmailContextDto(
                "test@example.com",
                "Test Subject",
                "Test Message");

        doThrow(new RuntimeException("Connection timeout")).when(emailSender).send(any(SimpleMailMessage.class));

        assertThatThrownBy(() -> emailService.sendMail(emailContext))
                .isInstanceOf(EmailSendingFailedException.class)
                .hasMessageContaining(String.format("Failed to send email to %s", emailContext.recipient()));
    }

    @Test
    void sendsMailWithEmptySubjectAndMessage() {
        EmailContextDto emailContext = new EmailContextDto(
                "test@example.com",
                "",
                "");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendMail(emailContext);

        verify(emailSender).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertThat(capturedMessage.getFrom()).isEqualTo("mbtiservice2@gmail.com");
        assertThat(capturedMessage.getTo()).containsExactly("test@example.com");
        assertThat(capturedMessage.getSubject()).isEmpty();
        assertThat(capturedMessage.getText()).isEmpty();
    }
}