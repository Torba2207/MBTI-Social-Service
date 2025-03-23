package com.pg.mbti.controllers;

import com.pg.mbti.dto.RegistrationRequestDto;
import com.pg.mbti.dto.RegistrationResponseDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.mappers.RegistrationMapper;
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
    private final RegistrationMapper registrationMapper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(
            @Valid @RequestBody final RegistrationRequestDto registrationDTO) {
        User user = registrationMapper.toEntity(registrationDTO);
        registrationService.registerUser(user);
        return ResponseEntity.ok(registrationMapper.toRegistrationResponseDto(user));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        registrationService.confirmEmail(token);
        return ResponseEntity.ok("Email confirmed successfully");
    }
}