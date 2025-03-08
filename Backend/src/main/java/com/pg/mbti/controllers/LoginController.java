package com.pg.mbti.controllers;

import com.pg.mbti.entity.login.LoginRequestDto;
import com.pg.mbti.entity.login.LoginResponseDto;
import com.pg.mbti.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService LoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(
            @RequestBody final LoginRequestDto LoginRequestDto
    ) {
        return ResponseEntity.ok(
                LoginService.authenticate(LoginRequestDto));
    }
}