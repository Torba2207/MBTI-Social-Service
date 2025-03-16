package com.pg.mbti.controllers;

import com.pg.mbti.dto.RegistrationRequestDto;
import com.pg.mbti.mappers.UserRegistrationMapper;
import com.pg.mbti.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody final RegistrationRequestDto registrationDTO) {

        registrationService.registerUser(userRegistrationMapper.toEntity(registrationDTO));

        return ResponseEntity.ok("User registered successfully.");
    }

    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String token) {
        registrationService.confirmEmail(token);
    }
}