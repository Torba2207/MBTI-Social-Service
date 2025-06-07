package com.pg.mbti;

import com.pg.mbti.controller.RegistrationController;
import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.dto.auth.RegistrationResponseDto;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.exception.EmailSendingFailedException;
import com.pg.mbti.exception.InvalidTokenException;
import com.pg.mbti.exception.ResourceNotFoundException;
import com.pg.mbti.exception.UserAlreadyExistsException;
import com.pg.mbti.util.mapper.RegistrationMapper;
import com.pg.mbti.service.auth.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private RegistrationMapper registrationMapper;

    @InjectMocks
    private RegistrationController registrationController;

    private RegistrationRequestDto registrationRequestDto;
    private RegistrationResponseDto registrationResponseDto;

    @BeforeEach
    void setUp() {
        registrationRequestDto = new RegistrationRequestDto(
                "Test",
                "User",
                "testuser",
                "Password123",
                "test@example.com",
                40.0,
                30.0,
                MBTIType.INTJ,
                Date.valueOf("2000-01-01"),
                Gender.MALE,
                Pronouns.HE_HIM
        );

        registrationResponseDto = new RegistrationResponseDto(
                "testuser",
                "test@example.com"
        );

        // Set client host and port values
        ReflectionTestUtils.setField(registrationController, "clientHost", "localhost");
        ReflectionTestUtils.setField(registrationController, "clientPort", "3000");
    }

    @Test
    void registerUserReturnsSuccessResponseWhenRegistrationSucceeds() {
        when(registrationMapper.toRegistrationResponseDto(registrationRequestDto)).thenReturn(registrationResponseDto);
        doNothing().when(registrationService).registerUser(registrationRequestDto);

        ResponseEntity<RegistrationResponseDto> response = registrationController.registerUser(registrationRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(registrationResponseDto);
        verify(registrationService).registerUser(registrationRequestDto);
        verify(registrationMapper).toRegistrationResponseDto(registrationRequestDto);
    }

    @Test
    void registerUserThrowsExceptionWhenUserAlreadyExists() {
        doThrow(new UserAlreadyExistsException("Nickname or Email already exists"))
                .when(registrationService).registerUser(registrationRequestDto);

        assertThatThrownBy(() -> registrationController.registerUser(registrationRequestDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Nickname or Email already exists");

        verify(registrationService).registerUser(registrationRequestDto);
        verifyNoMoreInteractions(registrationMapper);
    }

    @Test
    void registerUserThrowsExceptionWhenEmailSendingFails() {
        doThrow(new EmailSendingFailedException("Failed to send confirmation email"))
                .when(registrationService).registerUser(registrationRequestDto);

        assertThatThrownBy(() -> registrationController.registerUser(registrationRequestDto))
                .isInstanceOf(EmailSendingFailedException.class)
                .hasMessage("Failed to send confirmation email");

        verify(registrationService).registerUser(registrationRequestDto);
        verifyNoMoreInteractions(registrationMapper);
    }

    @Test
    void confirmEmailRedirectsToLoginWhenConfirmationSucceeds() {
        String token = "valid-token";
        doNothing().when(registrationService).confirmEmail(token);

        ResponseEntity<Void> response = registrationController.confirmEmail(token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(Objects.requireNonNull(response.getHeaders().getLocation()).toString())
                .isEqualTo("http://localhost:3000/loginPage?message=Email+confirmed+successfully");
        verify(registrationService).confirmEmail(token);
    }

    @Test
    void confirmEmailRedirectsToLoginWithErrorWhenUserNotFound() {
        String token = "token-with-nonexistent-user";
        doThrow(new ResourceNotFoundException("User not found"))
                .when(registrationService).confirmEmail(token);

        ResponseEntity<Void> response = registrationController.confirmEmail(token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(Objects.requireNonNull(response.getHeaders().getLocation()).toString())
                .contains("http://localhost:3000/loginPage?error=User+not+found");
        verify(registrationService).confirmEmail(token);
    }

    @Test
    void confirmEmailRedirectsToLoginWithErrorWhenTokenIsInvalid() {
        String token = "invalid-token";
        doThrow(new InvalidTokenException("Invalid or expired token"))
                .when(registrationService).confirmEmail(token);

        ResponseEntity<Void> response = registrationController.confirmEmail(token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(Objects.requireNonNull(response.getHeaders().getLocation()).toString())
                .contains("http://localhost:3000/loginPage?error=Invalid+or+expired+token");
        verify(registrationService).confirmEmail(token);
    }
}