package com.pg.mbti.controllers;

import com.pg.mbti.dto.ForgotPasswordDto;
import com.pg.mbti.dto.ResetPasswordDto;
import com.pg.mbti.dto.UpdatePasswordDto;
import com.pg.mbti.services.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordRequest) {
        passwordService.handleForgotPassword(forgotPasswordRequest.email());
        return ResponseEntity.ok("Password reset instructions sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordRequest) {
        passwordService.handleResetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordRequest, Authentication authentication) {
        String nickname = authentication.getName();
        passwordService.handleUpdatePassword(nickname, updatePasswordRequest);
        return ResponseEntity.ok("Password updated successfully");
    }
}