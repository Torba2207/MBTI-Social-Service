package com.pg.mbti;

import com.pg.mbti.dto.LoginRequestDto;
import com.pg.mbti.dto.LoginResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.EmailNotConfirmedException;
import com.pg.mbti.exceptions.InvalidPasswordException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.services.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginServiceTest {
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/MBTI_DATABASE");
        registry.add("spring.datasource.username", () -> "mbtiadmin");
        registry.add("spring.datasource.password", () -> "admin");
    }

    @Autowired
    private TestEntityManager entityManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginService loginService;

    private User regularUser;
    private User anonymousUser;

    @BeforeEach
    void setUp() {
        regularUser = User.builder()
                .nickname("testUser")
                .name("testUser")
                .surname("testUser")
                .password("password123")
                .email("test@example.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();

        anonymousUser = User.builder()
                .nickname("anonUser")
                .name("anonUser")
                .surname("anonUser")
                .password("password123")
                .email("anon@example.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.ANONYMOUS)
                .build();

        entityManager.persist(regularUser);
        entityManager.persist(anonymousUser);
        entityManager.flush();
    }

    @Test
    void authenticateWithUsernameSuccessfully() {
        LoginRequestDto loginRequest = new LoginRequestDto("testUser", "password123");

        when(usersRepository.findByNickname("testUser")).thenReturn(Optional.of(regularUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        LoginResponseDto response = loginService.authenticate(loginRequest, request, this.response);

        assertThat(response).isNotNull();
        assertThat(response.response()).isEqualTo("Login successful, session created.");
    }

    @Test
    void authenticateWithEmailSuccessfully() {
        LoginRequestDto loginRequest = new LoginRequestDto("test@example.com", "password123");

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(regularUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        LoginResponseDto response = loginService.authenticate(loginRequest, request, this.response);

        assertThat(response).isNotNull();
        assertThat(response.response()).isEqualTo("Login successful, session created.");
    }

    @Test
    void userNotFoundWithInvalidUsername() {
        LoginRequestDto loginRequest = new LoginRequestDto("nonexistentUser", "password123");

        when(usersRepository.findByNickname("nonexistentUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.authenticate(loginRequest, request, response))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void userNotFoundWithInvalidEmail() {
        LoginRequestDto loginRequest = new LoginRequestDto("nonexistent@example.com", "password123");

        when(usersRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.authenticate(loginRequest, request, response))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void invalidEmailFormat() {
        LoginRequestDto loginRequest = new LoginRequestDto("invalid-email", "password123");

        assertThatThrownBy(() -> loginService.authenticate(loginRequest, request, response))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void emailNotConfirmed() {
        LoginRequestDto loginRequest = new LoginRequestDto("anon@example.com", "password123");

        when(usersRepository.findByEmail("anon@example.com")).thenReturn(Optional.of(anonymousUser));

        assertThatThrownBy(() -> loginService.authenticate(loginRequest, request, response))
                .isInstanceOf(EmailNotConfirmedException.class)
                .hasMessage("Email not confirmed");
    }

    @Test
    void invalidPassword() {
        LoginRequestDto loginRequest = new LoginRequestDto("testUser", "wrongPassword");

        when(usersRepository.findByNickname("testUser")).thenReturn(Optional.of(regularUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> loginService.authenticate(loginRequest, request, response))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Invalid password");
    }
}