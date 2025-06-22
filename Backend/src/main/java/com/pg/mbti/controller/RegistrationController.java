package com.pg.mbti.controller;

import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.dto.auth.RegistrationResponseDto;
import com.pg.mbti.service.auth.RegistrationService;
import com.pg.mbti.util.mapper.RegistrationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Registration", description = "Endpoints for user registration and email confirmation")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;

    private final static String LOGIN_REDIRECT_URI = "/loginPage";

    @Value("${app.client.host}")
    private String clientHost;

    @Value("${app.client.port}")
    private String clientPort;

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
            description = "Confirms user email with token and redirects to login page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Email confirmed and redirected to login page"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> confirmEmail(
            @Parameter(description = "Email confirmation token", required = true)
            @RequestParam String token) {

        try {
            registrationService.confirmEmail(token);
            String redirectUrl = String.format("http://%s:%s%s?message=Email+confirmed+successfully",
                    clientHost, clientPort, LOGIN_REDIRECT_URI);
            return ResponseEntity.status(HttpStatus.FOUND.value())
                    .location(URI.create(redirectUrl))
                    .build();
        } catch (Exception e) {
            String redirectUrl = String.format("http://%s:%s%s?error=%s",
                    clientHost, clientPort, LOGIN_REDIRECT_URI,
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return ResponseEntity.status(HttpStatus.FOUND.value())
                    .location(URI.create(redirectUrl))
                    .build();
        }
    }
}