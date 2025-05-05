package com.pg.mbti.controller;

import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.dto.auth.RegistrationResponseDto;
import com.pg.mbti.util.mapper.RegistrationMapper;
import com.pg.mbti.service.auth.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Registration", description = "Endpoints for user registration and email confirmation")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Registers a new user and sends confirmation email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "User with this email or nickname already exists"),
            @ApiResponse(responseCode = "500", description = "Error sending email or internal server error")
    })
    public ResponseEntity<RegistrationResponseDto> registerUser(
            @Parameter(description = "Registration data for new user", required = true)
            @Valid @RequestBody final RegistrationRequestDto registrationDTO) {
        registrationService.registerUser(registrationDTO);
        return ResponseEntity.ok(registrationMapper.toRegistrationResponseDto(registrationDTO));
    }

    @GetMapping("/confirm-email")
    @Operation(summary = "Confirm email",
            description = "Confirms user email with token received via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email successfully confirmed",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> confirmEmail(
            @Parameter(description = "Email confirmation token", required = true)
            @RequestParam String token) {
        registrationService.confirmEmail(token);
        return ResponseEntity.ok("Email confirmed successfully");
    }
}