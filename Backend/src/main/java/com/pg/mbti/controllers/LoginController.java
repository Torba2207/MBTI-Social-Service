package com.pg.mbti.controllers;

import com.pg.mbti.dto.LoginRequestDto;
import com.pg.mbti.dto.LoginResponseDto;
import com.pg.mbti.services.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService LoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(
            @RequestBody final LoginRequestDto LoginRequestDto,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        session.setAttribute("nickname", LoginRequestDto.nickname());
        return ResponseEntity.ok(
                LoginService.authenticate(LoginRequestDto, request, response));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}