package com.pg.mbti.service.auth;

import com.pg.mbti.dto.auth.LoginRequestDto;
import com.pg.mbti.dto.auth.LoginResponseDto;
import com.pg.mbti.model.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exception.EmailNotConfirmedException;
import com.pg.mbti.exception.ResourceNotFoundException;
import com.pg.mbti.exception.InvalidPasswordException;
import com.pg.mbti.repository.UsersRepository;
import com.pg.mbti.util.validator.EmailValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final UsersRepository usersRepository;

    public LoginResponseDto authenticate(LoginRequestDto loginRequest, HttpServletRequest request, HttpServletResponse response) {
        User user = findUser(loginRequest.usernameOrEmail());

        if (user.getRole() == Role.ANONYMOUS) {
            throw new EmailNotConfirmedException("Email not confirmed");
        }

        authenticateUser(user.getNickname(), loginRequest.password(), request, response);

        return new LoginResponseDto("Login successful, session created.");
    }

    private User findUser(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            EmailValidator.validateEmailFormat(usernameOrEmail);
            return usersRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        return usersRepository.findByNickname(usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void authenticateUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password));

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
        } catch (Exception e) {
            throw new InvalidPasswordException("Invalid password");
        }
    }
}