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
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

/**
 * Service class for handling password-related operations, including resetting and updating passwords.
 */
@Service
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class PasswordService {

    private final SecureTokenUtil secureTokenUtil;
    private final EmailService emailService;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.email.reset-password-url}")
    private String resetPasswordUrl;

    /**
     * Handles the password reset process using a token.
     *
     * @param resetPasswordDto DTO containing the reset token and the new password.
     * @throws InvalidTokenException If the provided token is invalid or expired.
     * @throws ResourceNotFoundException If the user associated with the token is not found.
     */
    public void handleResetPassword(ResetPasswordDto resetPasswordDto) {
        log.info("Attempting to reset password with token."); // Log reset password attempt
        DtoValidator.validate(resetPasswordDto);

        String email = secureTokenUtil.getValue(resetPasswordDto.token())
                .orElseThrow(() -> {
                    log.warn("Invalid or expired token provided for password reset."); // Log invalid token
                    return new InvalidTokenException("Invalid token");
                });

        userRepository.updatePasswordByEmail(email, passwordEncoder.encode(resetPasswordDto.newPassword()));
        secureTokenUtil.deleteValue(resetPasswordDto.token());
        log.info("Password successfully reset for email: {}", email); // Log successful password reset
    }

    /**
     * Handles updating a user's password. Requires the old password for verification.
     *
     * @param nickname The nickname of the user whose password is to be updated.
     * @param updatePasswordDto DTO containing the old and new passwords.
     * @throws ResourceNotFoundException If the user is not found.
     * @throws InvalidPasswordException If the old password provided is incorrect.
     */
    public void handleUpdatePassword(String nickname, UpdatePasswordDto updatePasswordDto) {
        log.info("Attempting to update password for user: {}", nickname); // Log update password attempt
        DtoValidator.validate(updatePasswordDto);

        var user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> {
                    log.warn("User not found for nickname: {} during password update.", nickname); // Log user not found
                    return new ResourceNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(updatePasswordDto.oldPassword(), user.getPassword())) {
            log.warn("Invalid old password provided for user: {}", nickname); // Log incorrect old password
            throw new InvalidPasswordException("Old password is incorrect");
        }

        userRepository.updatePasswordByEmail(user.getEmail(),
                passwordEncoder.encode(updatePasswordDto.newPassword()));
        log.info("Password successfully updated for user: {}", nickname); // Log successful password update
    }

    /**
     * Handles the "forgot password" process by generating a reset token and sending a reset email.
     *
     * @param email The email address of the user who forgot their password.
     * @throws EmailSendingFailedException If there is an issue sending the reset password email.
     * @throws InvalidTokenException If a token cannot be generated.
     */
    public void handleForgotPassword(String email) {
        log.info("Initiating forgot password process for email: {}", email); // Log forgot password initiation
        try {
            String token = secureTokenUtil.generateToken(email, 1, TimeUnit.DAYS)
                    .orElseThrow(() -> {
                        log.error("Failed to generate token for email: {}", email); // Log token generation failure
                        return new InvalidTokenException("Invalid token");
                    });
            String resetLink = resetPasswordUrl + token;
            EmailContextDto emailContext = new EmailContextDto(email,
                    "Reset Password", "Click the link to reset your password: " + resetLink);
            emailService.sendMail(emailContext);
            log.info("Reset password email sent to: {}", email); // Log successful email send
        } catch (Exception e) {
            log.error("Failed to send reset password email to {}: {}", email, e.getMessage()); // Log email sending failure
            throw new EmailSendingFailedException("Failed to send reset password email: " + e.getMessage());
        }
    }
}