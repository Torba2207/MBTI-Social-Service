package com.pg.mbti.services;

import com.pg.mbti.dto.auth.LoginRequestDto;
import com.pg.mbti.dto.auth.LoginResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.EmailNotConfirmedException;
import com.pg.mbti.exceptions.InvalidEmailException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.InvalidPasswordException;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.services.email.EmailValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final UsersRepository usersRepository;

    public LoginResponseDto authenticate(
            LoginRequestDto loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        String usernameOrEmail = loginRequest.usernameOrEmail();
        User user;

        if (usernameOrEmail.contains("@")) {
            try {
                EmailValidator.validateEmailFormat(usernameOrEmail);
            } catch (InvalidEmailException e) {
                throw new ResourceNotFoundException("User not found");
            }

            user = usersRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            user = usersRepository.findByNickname(usernameOrEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        if (user.getRole() == Role.ANONYMOUS) {
            throw new EmailNotConfirmedException("Email not confirmed");
        }

        try {
            UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(
                    user.getNickname(), loginRequest.password());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
        } catch (Exception e) {
            throw new InvalidPasswordException("Invalid password");
        }

        return new LoginResponseDto("Login successful, session created.");
    }
}