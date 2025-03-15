package com.pg.mbti.controllers;

import com.pg.mbti.dto.ForgotPasswordDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordController {

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordRequest) {
        // TODO: Implement sending email with password reset link
        return null;
    }


}
