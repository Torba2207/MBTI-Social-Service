package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendMail(EmailContextDto emailContext) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mbtiservice2@gmail.com");
            message.setTo(emailContext.recipient());
            message.setSubject(emailContext.subject());
            message.setText(emailContext.message());

            emailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingFailedException(String.format("Failed to send email to %s", emailContext.recipient()));
        }
    }
}