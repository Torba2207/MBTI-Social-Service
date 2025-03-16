package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.dto.ResetPasswordDto;
import com.pg.mbti.dto.UpdatePasswordDto;
import com.pg.mbti.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class PasswordService {

    private final SecureTokenService secureTokenService;
    private final EmailService emailService;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handleForgotPassword(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User not found");
        }
        String token = secureTokenService.generateToken(email, 1, TimeUnit.DAYS);
        // TODO replace with accurate domain
        String resetLink = "http://yourdomain.com/reset-password?token=" + token;
        EmailContextDto emailContext = new EmailContextDto(email,
                "Reset Password", "Click the link to reset your password: " + resetLink);
        emailService.sendMail(emailContext);
    }

    public void handleResetPassword(ResetPasswordDto resetPasswordRequest) {
        String email = secureTokenService.getValue(resetPasswordRequest.token());
        if (email != null) {
            userRepository.updatePasswordByEmail(email, passwordEncoder.encode(resetPasswordRequest.newPassword()));
            secureTokenService.deleteValue(resetPasswordRequest.token());
        } else {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }

    public void handleUpdatePassword(String nickname, UpdatePasswordDto updatePasswordRequest) {
        var user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (passwordEncoder.matches(updatePasswordRequest.oldPassword(), user.getPassword())) {
            userRepository.updatePasswordByEmail(user.getEmail(), passwordEncoder.encode(updatePasswordRequest.newPassword()));
        } else {
            throw new IllegalArgumentException("Old password is incorrect");
        }
    }
}