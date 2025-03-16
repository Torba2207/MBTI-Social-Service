package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendMail(EmailContextDto emailContext) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("");
        message.setTo(emailContext.recipient());
        message.setSubject(emailContext.subject());
        message.setText(emailContext.message());

        emailSender.send(message);
    }
}
