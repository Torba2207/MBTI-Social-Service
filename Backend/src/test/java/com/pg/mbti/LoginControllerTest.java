package com.pg.mbti;

import com.pg.mbti.controllers.LoginController;
import com.pg.mbti.dto.auth.LoginRequestDto;
import com.pg.mbti.dto.auth.LoginResponseDto;
import com.pg.mbti.exceptions.EmailNotConfirmedException;
import com.pg.mbti.exceptions.InvalidPasswordException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.services.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
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
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginController loginController;

    private LoginRequestDto loginRequestDto;
    private LoginResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        loginRequestDto = new LoginRequestDto("testUser", "password123");
        loginResponseDto = new LoginResponseDto("Login successful, session created.");
    }

    @Test
    void authenticateReturnsLoginResponseDtoWhenCredentialsAreValid() {
        when(loginService.authenticate(eq(loginRequestDto), eq(request), eq(response)))
                .thenReturn(loginResponseDto);

        ResponseEntity<LoginResponseDto> result = loginController.authenticate(
                loginRequestDto, request, response, session);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(loginResponseDto);
        verify(session).setAttribute("nickname", loginRequestDto.usernameOrEmail());
        verify(loginService).authenticate(loginRequestDto, request, response);
    }

    @Test
    void authenticateThrowsExceptionWhenUserNotFound() {
        when(loginService.authenticate(eq(loginRequestDto), eq(request), eq(response)))
                .thenThrow(new ResourceNotFoundException("User not found"));

        assertThatThrownBy(() ->
                loginController.authenticate(loginRequestDto, request, response, session))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(session).setAttribute("nickname", loginRequestDto.usernameOrEmail());
    }

    @Test
    void authenticateThrowsExceptionWhenEmailNotConfirmed() {
        when(loginService.authenticate(eq(loginRequestDto), eq(request), eq(response)))
                .thenThrow(new EmailNotConfirmedException("Email not confirmed"));

        assertThatThrownBy(() ->
                loginController.authenticate(loginRequestDto, request, response, session))
                .isInstanceOf(EmailNotConfirmedException.class)
                .hasMessage("Email not confirmed");

        verify(session).setAttribute("nickname", loginRequestDto.usernameOrEmail());
    }

    @Test
    void authenticateThrowsExceptionWhenPasswordInvalid() {
        when(loginService.authenticate(eq(loginRequestDto), eq(request), eq(response)))
                .thenThrow(new InvalidPasswordException("Invalid password"));

        assertThatThrownBy(() ->
                loginController.authenticate(loginRequestDto, request, response, session))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Invalid password");

        verify(session).setAttribute("nickname", loginRequestDto.usernameOrEmail());
    }

    @Test
    void logoutInvalidatesSessionAndReturnsOkResponse() {
        ResponseEntity<Void> result = loginController.logout(session);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(session).invalidate();
    }
}