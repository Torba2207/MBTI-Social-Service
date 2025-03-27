package com.pg.mbti.controllers;

import com.pg.mbti.dto.LoginRequestDto;
import com.pg.mbti.dto.LoginResponseDto;
import com.pg.mbti.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and session management")
public class LoginController {

    private final LoginService LoginService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user",
            description = "Authenticates a user with username/email and password and creates a session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Email not confirmed"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LoginResponseDto> authenticate(
            @Parameter(description = "Login credentials (username/email and password)",
                    required = true)
            @RequestBody final LoginRequestDto LoginRequestDto,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        session.setAttribute("nickname", LoginRequestDto.usernameOrEmail());
        return ResponseEntity.ok(
                LoginService.authenticate(LoginRequestDto, request, response));
    }

    @GetMapping("/logout")
    @Operation(summary = "Logout user",
            description = "Invalidates the user's session and logs them out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}