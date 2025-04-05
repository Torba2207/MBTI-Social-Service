package com.pg.mbti;

import com.pg.mbti.controllers.RegistrationController;
import com.pg.mbti.dto.RegistrationRequestDto;
import com.pg.mbti.dto.RegistrationResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.UserAlreadyExistsException;
import com.pg.mbti.mappers.RegistrationMapper;
import com.pg.mbti.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Test
    void registerUserSuccessfully() {
        // Prepare test data
        RegistrationRequestDto requestDto = new RegistrationRequestDto(

        );
        User user = new User();
        RegistrationResponseDto responseDto = new RegistrationResponseDto();

        // Configure mocks
        when(registrationMapper.toEntity(requestDto)).thenReturn(user);
        when(registrationMapper.toRegistrationResponseDto(user)).thenReturn(responseDto);
        doNothing().when(registrationService).registerUser(user);

        // Execute
        ResponseEntity<RegistrationResponseDto> response = registrationController.registerUser(requestDto);

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(responseDto);
        verify(registrationService).registerUser(user);
    }

    @Test
    void registerUserWhenUserAlreadyExists() {
        // Prepare test data
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        User user = new User();

        // Configure mocks
        when(registrationMapper.toEntity(requestDto)).thenReturn(user);
        doThrow(new UserAlreadyExistsException("User already exists")).when(registrationService).registerUser(user);

        // Execute and verify
        assertThatThrownBy(() -> registrationController.registerUser(requestDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already exists");
    }

    @Test
    void registerUserWhenEmailSendingFails() {
        // Prepare test data
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        User user = new User();

        // Configure mocks
        when(registrationMapper.toEntity(requestDto)).thenReturn(user);
        doThrow(new EmailSendingFailedException("Failed to send email")).when(registrationService).registerUser(user);

        // Execute and verify
        assertThatThrownBy(() -> registrationController.registerUser(requestDto))
                .isInstanceOf(EmailSendingFailedException.class)
                .hasMessageContaining("Failed to send email");
    }

    @Test
    void confirmEmailSuccessfully() {
        // Prepare test data
        String token = "valid-token";
        doNothing().when(registrationService).confirmEmail(token);

        // Execute
        ResponseEntity<String> response = registrationController.confirmEmail(token);

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Email confirmed successfully");
        verify(registrationService).confirmEmail(token);
    }

    @Test
    void confirmEmailWithInvalidToken() {
        // Prepare test data
        String token = "invalid-token";
        doThrow(new InvalidTokenException("Invalid token")).when(registrationService).confirmEmail(token);

        // Execute and verify
        assertThatThrownBy(() -> registrationController.confirmEmail(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Invalid token");
    }

    @Test
    void confirmEmailWhenUserNotFound() {
        // Prepare test data
        String token = "token-without-user";
        doThrow(new ResourceNotFoundException("User not found")).when(registrationService).confirmEmail(token);

        // Execute and verify
        assertThatThrownBy(() -> registrationController.confirmEmail(token))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}