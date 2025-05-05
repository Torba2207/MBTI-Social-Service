package com.pg.mbti.controller;

import com.pg.mbti.dto.password.ForgotPasswordDto;
import com.pg.mbti.dto.password.ResetPasswordDto;
import com.pg.mbti.dto.password.UpdatePasswordDto;
import com.pg.mbti.service.auth.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Password Management", description = "Endpoints for password recovery and management")
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot-password")
    @Operation(summary = "Initiate password reset",
            description = "Sends a password reset link to the user's email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset instructions sent successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "User with given email not found"),
            @ApiResponse(responseCode = "500", description = "Error sending email or internal server error")
    })
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "User's email for password reset", required = true)
            @RequestBody ForgotPasswordDto forgotPasswordRequest) {
        passwordService.handleForgotPassword(forgotPasswordRequest.email());
        return ResponseEntity.ok("Password reset instructions sent to email");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using token",
            description = "Resets the user's password using a valid token received via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Token and new password information", required = true)
            @RequestBody ResetPasswordDto resetPasswordRequest) {
        passwordService.handleResetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/update-password")
    @Operation(summary = "Update user password",
            description = "Updates the authenticated user's password after verifying the old password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Old password is incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> updatePassword(
            @Parameter(description = "Old and new password information", required = true)
            @RequestBody UpdatePasswordDto updatePasswordRequest,
            @Parameter(description = "Authentication information of the current user")
            Authentication authentication) {
        passwordService.handleUpdatePassword(authentication.getName(), updatePasswordRequest);
        return ResponseEntity.ok("Password updated successfully");
    }
}