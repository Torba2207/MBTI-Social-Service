package com.pg.mbti;

import com.pg.mbti.controllers.RegistrationController;
import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.dto.auth.RegistrationResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.UserAlreadyExistsException;
import com.pg.mbti.mappers.RegistrationMapper;
import com.pg.mbti.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;

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
    private User user;

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

        user = User.builder()
                .nickname("testuser")
                .name("Test")
                .surname("User")
                .email("test@example.com")
                .password("encodedPassword")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .latitude(40.0)
                .longitude(30.0)
                .role(Role.ANONYMOUS)
                .build();

        registrationResponseDto = new RegistrationResponseDto(
                "testuser",
                "test@example.com"
        );
    }

    @Test
    void registerUserReturnsSuccessResponseWhenRegistrationSucceeds() {
        when(registrationMapper.toEntity(registrationRequestDto)).thenReturn(user);
        when(registrationMapper.toRegistrationResponseDto(user)).thenReturn(registrationResponseDto);
        doNothing().when(registrationService).registerUser(user);

        ResponseEntity<RegistrationResponseDto> response = registrationController.registerUser(registrationRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(registrationResponseDto);
        verify(registrationMapper).toEntity(registrationRequestDto);
        verify(registrationService).registerUser(user);
        verify(registrationMapper).toRegistrationResponseDto(user);
    }

    @Test
    void registerUserThrowsExceptionWhenUserAlreadyExists() {
        when(registrationMapper.toEntity(registrationRequestDto)).thenReturn(user);
        doThrow(new UserAlreadyExistsException("Nickname or Email already exists"))
                .when(registrationService).registerUser(user);

        assertThatThrownBy(() -> registrationController.registerUser(registrationRequestDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Nickname or Email already exists");

        verify(registrationMapper).toEntity(registrationRequestDto);
        verify(registrationService).registerUser(user);
        verifyNoMoreInteractions(registrationMapper);
    }

    @Test
    void registerUserThrowsExceptionWhenEmailSendingFails() {
        when(registrationMapper.toEntity(registrationRequestDto)).thenReturn(user);
        doThrow(new EmailSendingFailedException("Failed to send confirmation email"))
                .when(registrationService).registerUser(user);

        assertThatThrownBy(() -> registrationController.registerUser(registrationRequestDto))
                .isInstanceOf(EmailSendingFailedException.class)
                .hasMessage("Failed to send confirmation email");

        verify(registrationMapper).toEntity(registrationRequestDto);
        verify(registrationService).registerUser(user);
        verifyNoMoreInteractions(registrationMapper);
    }

    @Test
    void confirmEmailReturnsSuccessResponseWhenConfirmationSucceeds() {
        String token = "valid-token";
        doNothing().when(registrationService).confirmEmail(token);

        ResponseEntity<String> response = registrationController.confirmEmail(token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Email confirmed successfully");
        verify(registrationService).confirmEmail(token);
    }

    @Test
    void confirmEmailThrowsExceptionWhenTokenIsInvalid() {
        String token = "invalid-token";
        doThrow(new InvalidTokenException("Invalid or expired token"))
                .when(registrationService).confirmEmail(token);

        assertThatThrownBy(() -> registrationController.confirmEmail(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid or expired token");

        verify(registrationService).confirmEmail(token);
    }

    @Test
    void confirmEmailThrowsExceptionWhenUserNotFound() {
        String token = "token-with-nonexistent-user";
        doThrow(new ResourceNotFoundException("User not found"))
                .when(registrationService).confirmEmail(token);

        assertThatThrownBy(() -> registrationController.confirmEmail(token))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(registrationService).confirmEmail(token);
    }
}