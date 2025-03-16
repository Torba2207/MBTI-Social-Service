package com.pg.mbti.services;

import com.pg.mbti.dto.LoginRequestDto;
import com.pg.mbti.dto.LoginResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.repositories.UsersRepository;
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
        User user = usersRepository.findByNickname(loginRequest.nickname())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() == Role.ANONYMOUS) {
            throw new IllegalArgumentException("Email not confirmed");
        }
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.nickname(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return new LoginResponseDto("Login successful, session created.");
    }
}