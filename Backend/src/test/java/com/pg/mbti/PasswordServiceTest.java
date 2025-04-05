package com.pg.mbti;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.dto.ResetPasswordDto;
import com.pg.mbti.dto.UpdatePasswordDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidPasswordException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.services.PasswordService;
import com.pg.mbti.services.SecureTokenService;
import com.pg.mbti.services.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

    @Mock
    private SecureTokenService secureTokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private UsersRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(passwordService, "resetPasswordUrl", "http://localhost:3000/reset-password?token=");
    }

    @Test
    void forgotPasswordSendsResetLinkEmailSuccessfully() {
        String email = "test@example.com";
        String token = "generatedToken123";

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(secureTokenService.generateToken(eq(email), eq(1), eq(TimeUnit.DAYS))).thenReturn(token);

        ArgumentCaptor<EmailContextDto> emailCaptor = ArgumentCaptor.forClass(EmailContextDto.class);

        passwordService.handleForgotPassword(email);

        verify(emailService).sendMail(emailCaptor.capture());
        EmailContextDto capturedEmail = emailCaptor.getValue();

        assertThat(capturedEmail.recipient()).isEqualTo(email);
        assertThat(capturedEmail.subject()).isEqualTo("Reset Password");
        assertThat(capturedEmail.message()).contains("http://localhost:3000/reset-password?token=generatedToken123");
    }

    @Test
    void forgotPasswordThrowsExceptionWhenUserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThatThrownBy(() -> passwordService.handleForgotPassword(email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(secureTokenService, never()).generateToken(anyString(), anyInt(), any(TimeUnit.class));
        verify(emailService, never()).sendMail(any(EmailContextDto.class));
    }

    @Test
    void forgotPasswordThrowsExceptionWhenEmailSendingFails() {
        String email = "test@example.com";
        String token = "generatedToken123";

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(secureTokenService.generateToken(eq(email), eq(1), eq(TimeUnit.DAYS))).thenReturn(token);
        doThrow(new RuntimeException("SMTP error")).when(emailService).sendMail(any(EmailContextDto.class));

        assertThatThrownBy(() -> passwordService.handleForgotPassword(email))
                .isInstanceOf(EmailSendingFailedException.class)
                .hasMessageContaining("Failed to send reset password email");
    }

    @Test
    void resetPasswordSuccessfullyWithValidToken() {
        String email = "test@example.com";
        String token = "validToken123";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedPassword123";

        ResetPasswordDto resetRequest = new ResetPasswordDto(token, newPassword);

        when(secureTokenService.getValue(token)).thenReturn(email);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        passwordService.handleResetPassword(resetRequest);

        verify(userRepository).updatePasswordByEmail(email, encodedPassword);
        verify(secureTokenService).deleteValue(token);
    }

    @Test
    void resetPasswordThrowsExceptionWithInvalidToken() {
        String token = "invalidToken123";
        String newPassword = "newPassword123";

        ResetPasswordDto resetRequest = new ResetPasswordDto(token, newPassword);

        when(secureTokenService.getValue(token)).thenReturn(null);

        assertThatThrownBy(() -> passwordService.handleResetPassword(resetRequest))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid or expired token");

        verify(userRepository, never()).updatePasswordByEmail(anyString(), anyString());
        verify(secureTokenService, never()).deleteValue(anyString());
    }

    @Test
    void updatePasswordSuccessfullyWithCorrectOldPassword() {
        String nickname = "testUser";
        String email = "test@example.com";
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword123";
        String encodedNewPassword = "encodedNewPassword123";
        String storedPassword = "storedPassword123";

        UpdatePasswordDto updateRequest = new UpdatePasswordDto(oldPassword, newPassword);
        User user = new User();
        user.setEmail(email);
        user.setPassword(storedPassword);

        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, storedPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        passwordService.handleUpdatePassword(nickname, updateRequest);

        verify(userRepository).updatePasswordByEmail(email, encodedNewPassword);
    }

    @Test
    void updatePasswordThrowsExceptionWhenUserNotFound() {
        String nickname = "nonexistentUser";
        UpdatePasswordDto updateRequest = new UpdatePasswordDto("oldPassword", "newPassword");

        when(userRepository.findByNickname(nickname)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordService.handleUpdatePassword(nickname, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).updatePasswordByEmail(anyString(), anyString());
    }

    @Test
    void updatePasswordThrowsExceptionWithIncorrectOldPassword() {
        String nickname = "testUser";
        String email = "test@example.com";
        String oldPassword = "wrongPassword";
        String newPassword = "newPassword123";
        String storedPassword = "storedPassword123";

        UpdatePasswordDto updateRequest = new UpdatePasswordDto(oldPassword, newPassword);
        User user = new User();
        user.setEmail(email);
        user.setPassword(storedPassword);

        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, storedPassword)).thenReturn(false);

        assertThatThrownBy(() -> passwordService.handleUpdatePassword(nickname, updateRequest))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Old password is incorrect");

        verify(userRepository, never()).updatePasswordByEmail(anyString(), anyString());
    }
}