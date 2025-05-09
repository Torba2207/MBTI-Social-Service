package com.pg.mbti.service.auth;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.dto.password.ResetPasswordDto;
import com.pg.mbti.dto.password.UpdatePasswordDto;
import com.pg.mbti.exception.EmailSendingFailedException;
import com.pg.mbti.exception.InvalidPasswordException;
import com.pg.mbti.exception.InvalidTokenException;
import com.pg.mbti.exception.ResourceNotFoundException;
import com.pg.mbti.repository.UsersRepository;
import com.pg.mbti.service.EmailService;
import com.pg.mbti.util.SecureTokenUtil;
import com.pg.mbti.util.validator.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final SecureTokenUtil secureTokenUtil;
    private final EmailService emailService;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.email.reset-password-url}")
    private String resetPasswordUrl;

    public void handleResetPassword(ResetPasswordDto resetPasswordDto) {
        DtoValidator.validate(resetPasswordDto);

        String email = secureTokenUtil.getValue(resetPasswordDto.token())
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        userRepository.updatePasswordByEmail(email, passwordEncoder.encode(resetPasswordDto.newPassword()));
        secureTokenUtil.deleteValue(resetPasswordDto.token());
    }

    public void handleUpdatePassword(String nickname, UpdatePasswordDto updatePasswordDto) {
        DtoValidator.validate(updatePasswordDto);

        var user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(updatePasswordDto.oldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        userRepository.updatePasswordByEmail(user.getEmail(),
                passwordEncoder.encode(updatePasswordDto.newPassword()));
    }

    public void handleForgotPassword(String email) {
        try {
            String token = secureTokenUtil.generateToken(email, 1, TimeUnit.DAYS)
                    .orElseThrow(() -> new InvalidTokenException("Invalid token"));
            String resetLink = resetPasswordUrl + token;
            EmailContextDto emailContext = new EmailContextDto(email,
                    "Reset Password", "Click the link to reset your password: " + resetLink);
            emailService.sendMail(emailContext);
        } catch (Exception e) {
            throw new EmailSendingFailedException("Failed to send reset password email: " + e.getMessage());
        }
    }
}
