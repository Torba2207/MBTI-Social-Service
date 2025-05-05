package com.pg.mbti.service;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.exception.EmailSendingFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.email}")
    private String email;

    public void sendMail(EmailContextDto emailContext) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(emailContext.recipient());
            message.setSubject(emailContext.subject());
            message.setText(emailContext.message());

            emailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingFailedException(String.format("Failed to send email to %s", emailContext.recipient()));
        }
    }
}