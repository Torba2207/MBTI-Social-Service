package com.pg.mbti.util;

import com.pg.mbti.model.User;
import com.pg.mbti.exception.InvalidPasswordException;
import com.pg.mbti.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.ArrayList;

/**
 * Custom Spring Security {@link AuthenticationProvider} for authenticating users
 * based on their username (nickname) and password.
 */
@Component
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class UserAuthenticationProvider implements AuthenticationProvider {
    private final UsersService usersService; // Service to retrieve user details
    private final PasswordEncoder passwordEncoder; // Encoder for password matching

    /**
     * Performs authentication with the provided {@link Authentication} object.
     *
     * @param authentication The authentication request object, typically a {@link UsernamePasswordAuthenticationToken}.
     * @return A fully authenticated {@link Authentication} object.
     * @throws AuthenticationException If authentication fails (e.g., user not found, invalid password).
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // Get the username (nickname) from the authentication object
        String password = authentication.getCredentials().toString(); // Get the raw password from the authentication object

        log.debug("Attempting to authenticate user: {}", username); // Log the authentication attempt

        // Retrieve user details by nickname
        User userDetails = usersService.getUserByNickname(username);
        log.debug("User details retrieved for user: {}", username); // Log successful user details retrieval

        // Check if the provided raw password matches the encoded password stored for the user
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            log.warn("Authentication failed for user {}: Invalid password.", username); // Log invalid password attempt
            throw new InvalidPasswordException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }

    /**
     * Indicates whether this {@link AuthenticationProvider} supports the given {@link Authentication} type.
     *
     * @param authentication The class of the Authentication object to check.
     * @return {@code true} if this provider can handle the submitted Authentication type, {@code false} otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // This provider supports UsernamePasswordAuthenticationToken for username/password based authentication
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}