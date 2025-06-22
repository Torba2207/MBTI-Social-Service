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
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

/**
 * Service class responsible for handling user login and authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final UsersRepository usersRepository;

    /**
     * Authenticates a user based on the provided login credentials.
     *
     * @param loginRequest The DTO containing the username/email and password.
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @return A LoginResponseDto indicating the success of the login operation.
     * @throws EmailNotConfirmedException If the user's email has not been confirmed.
     * @throws ResourceNotFoundException If the user is not found.
     * @throws InvalidPasswordException If the provided password is incorrect.
     */
    public LoginResponseDto authenticate(LoginRequestDto loginRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("Attempting to authenticate user: {}", loginRequest.usernameOrEmail()); // Log the authentication attempt
        User user = findUser(loginRequest.usernameOrEmail());

        if (user.getRole() == Role.ANONYMOUS) {
            log.warn("Authentication failed for user {}: Email not confirmed.", loginRequest.usernameOrEmail()); // Log email not confirmed
            throw new EmailNotConfirmedException("Email not confirmed");
        }

        authenticateUser(user.getNickname(), loginRequest.password(), request, response);
        log.info("User {} successfully authenticated.", loginRequest.usernameOrEmail()); // Log successful authentication

        return new LoginResponseDto("Login successful, session created.");
    }

    /**
     * Finds a user by their username or email.
     *
     * @param usernameOrEmail The username or email of the user.
     * @return The User object if found.
     * @throws ResourceNotFoundException If the user is not found.
     */
    private User findUser(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            log.debug("Attempting to find user by email: {}", usernameOrEmail); // Log search by email
            EmailValidator.validateEmailFormat(usernameOrEmail);
            return usersRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> {
                        log.warn("User not found with email: {}", usernameOrEmail); // Log user not found by email
                        return new ResourceNotFoundException("User not found");
                    });
        }
        log.debug("Attempting to find user by nickname: {}", usernameOrEmail); // Log search by nickname
        return usersRepository.findByNickname(usernameOrEmail)
                .orElseThrow(() -> {
                    log.warn("User not found with nickname: {}", usernameOrEmail); // Log user not found by nickname
                    return new ResourceNotFoundException("User not found");
                });
    }

    /**
     * Performs the actual Spring Security authentication.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws InvalidPasswordException If authentication fails due to incorrect credentials.
     */
    private void authenticateUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.debug("Authenticating user {} using Spring Security.", username); // Log Spring Security authentication attempt
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password));

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            log.debug("Security context saved for user {}.", username); // Log security context saved
        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", username, e.getMessage()); // Log authentication failure
            throw new InvalidPasswordException("Invalid password");
        }
    }
}