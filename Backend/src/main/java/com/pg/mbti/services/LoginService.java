package com.pg.mbti.services;

import com.pg.mbti.entity.login.LoginRequestDto;
import com.pg.mbti.entity.login.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDto authenticate(
            final LoginRequestDto request) {

        final var authToken = UsernamePasswordAuthenticationToken
                .unauthenticated(request.username(), request.password());

        authenticationManager.authenticate(authToken);

        final var token = jwtService.generateToken(request.username());
        return new LoginResponseDto(token);
    }
}