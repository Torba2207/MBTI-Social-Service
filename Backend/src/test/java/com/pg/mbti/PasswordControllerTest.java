package com.pg.mbti;

import com.pg.mbti.controllers.PasswordController;
import com.pg.mbti.dto.password.ForgotPasswordDto;
import com.pg.mbti.dto.password.ResetPasswordDto;
import com.pg.mbti.dto.password.UpdatePasswordDto;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidPasswordException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.services.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

    @Mock
    private PasswordService passwordService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PasswordController passwordController;

    private ForgotPasswordDto forgotPasswordDto;
    private ResetPasswordDto resetPasswordDto;
    private UpdatePasswordDto updatePasswordDto;

    @BeforeEach
    void setUp() {
        forgotPasswordDto = new ForgotPasswordDto("test@example.com");
        resetPasswordDto = new ResetPasswordDto("validToken123", "newPassword123");
        updatePasswordDto = new UpdatePasswordDto("oldPassword123", "newPassword123");

        lenient().when(authentication.getName()).thenReturn("user1");
    }

    @Test
    void forgotPasswordReturnsSuccessWhenEmailSent() {
        doNothing().when(passwordService).handleForgotPassword("test@example.com");

        ResponseEntity<String> response = passwordController.forgotPassword(forgotPasswordDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Password reset instructions sent to email");
        verify(passwordService).handleForgotPassword("test@example.com");
    }

    @Test
    void forgotPasswordThrowsExceptionWhenUserNotFound() {
        doThrow(new ResourceNotFoundException("User not found"))
                .when(passwordService).handleForgotPassword("test@example.com");

        assertThatThrownBy(() -> passwordController.forgotPassword(forgotPasswordDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(passwordService).handleForgotPassword("test@example.com");
    }

    @Test
    void forgotPasswordThrowsExceptionWhenEmailSendingFails() {
        doThrow(new EmailSendingFailedException("Failed to send reset password email"))
                .when(passwordService).handleForgotPassword("test@example.com");

        assertThatThrownBy(() -> passwordController.forgotPassword(forgotPasswordDto))
                .isInstanceOf(EmailSendingFailedException.class)
                .hasMessageContaining("Failed to send reset password email");

        verify(passwordService).handleForgotPassword("test@example.com");
    }

    @Test
    void resetPasswordReturnsSuccessWhenPasswordReset() {
        doNothing().when(passwordService).handleResetPassword(resetPasswordDto);

        ResponseEntity<String> response = passwordController.resetPassword(resetPasswordDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Password reset successfully");
        verify(passwordService).handleResetPassword(resetPasswordDto);
    }

    @Test
    void resetPasswordThrowsExceptionWithInvalidToken() {
        doThrow(new InvalidTokenException("Invalid or expired token"))
                .when(passwordService).handleResetPassword(resetPasswordDto);

        assertThatThrownBy(() -> passwordController.resetPassword(resetPasswordDto))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid or expired token");

        verify(passwordService).handleResetPassword(resetPasswordDto);
    }

    @Test
    void updatePasswordReturnsSuccessWhenPasswordUpdated() {
        doNothing().when(passwordService).handleUpdatePassword("user1", updatePasswordDto);

        ResponseEntity<String> response = passwordController.updatePassword(updatePasswordDto, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Password updated successfully");
        verify(passwordService).handleUpdatePassword("user1", updatePasswordDto);
    }

    @Test
    void updatePasswordThrowsExceptionWhenUserNotFound() {
        doThrow(new ResourceNotFoundException("User not found"))
                .when(passwordService).handleUpdatePassword("user1", updatePasswordDto);

        assertThatThrownBy(() -> passwordController.updatePassword(updatePasswordDto, authentication))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(passwordService).handleUpdatePassword("user1", updatePasswordDto);
    }

    @Test
    void updatePasswordThrowsExceptionWithIncorrectPassword() {
        doThrow(new InvalidPasswordException("Old password is incorrect"))
                .when(passwordService).handleUpdatePassword("user1", updatePasswordDto);

        assertThatThrownBy(() -> passwordController.updatePassword(updatePasswordDto, authentication))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Old password is incorrect");

        verify(passwordService).handleUpdatePassword("user1", updatePasswordDto);
    }
}