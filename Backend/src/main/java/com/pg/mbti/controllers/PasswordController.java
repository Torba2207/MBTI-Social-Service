package com.pg.mbti.controllers;

import com.pg.mbti.dto.ForgotPasswordDto;
import com.pg.mbti.dto.ResetPasswordDto;
import com.pg.mbti.dto.UpdatePasswordDto;
import com.pg.mbti.services.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordRequest) {
        passwordService.handleForgotPassword(forgotPasswordRequest.email());
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordRequest) {
        passwordService.handleResetPassword(resetPasswordRequest);
    }

    @PostMapping("/update-password")
    public void updatePassword(@RequestBody UpdatePasswordDto updatePasswordRequest, Authentication authentication) {
        String nickname = authentication.getName();
        passwordService.handleUpdatePassword(nickname, updatePasswordRequest);
    }
}
