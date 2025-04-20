package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.dto.password.ResetPasswordDto;
import com.pg.mbti.dto.password.UpdatePasswordDto;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidPasswordException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final SecureTokenService secureTokenService;
    private final EmailService emailService;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.email.reset-password-url}")
    private String resetPasswordUrl;

    public void handleForgotPassword(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("User not found");
        }

        try {
            int tokenExpirationTime = 1;
            String token = secureTokenService.generateToken(email, tokenExpirationTime, TimeUnit.DAYS);
            String resetLink = resetPasswordUrl + token;
            EmailContextDto emailContext = new EmailContextDto(email,
                    "Reset Password", "Click the link to reset your password: " + resetLink);
            emailService.sendMail(emailContext);
        } catch (Exception e) {
            throw new EmailSendingFailedException("Failed to send reset password email: " + e.getMessage());
        }
    }

    public void handleResetPassword(ResetPasswordDto resetPasswordRequest) {
        String email = secureTokenService.getValue(resetPasswordRequest.token());
        if (email != null) {
            userRepository.updatePasswordByEmail(email, passwordEncoder.encode(resetPasswordRequest.newPassword()));
            secureTokenService.deleteValue(resetPasswordRequest.token());
        } else {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }

    public void handleUpdatePassword(String nickname, UpdatePasswordDto updatePasswordRequest) {
        var user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (passwordEncoder.matches(updatePasswordRequest.oldPassword(), user.getPassword())) {
            userRepository.updatePasswordByEmail(user.getEmail(), passwordEncoder.encode(updatePasswordRequest.newPassword()));
        } else {
            throw new InvalidPasswordException("Old password is incorrect");
        }
    }
}