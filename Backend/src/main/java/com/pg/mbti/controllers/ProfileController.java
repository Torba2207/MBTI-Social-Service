package com.pg.mbti.controllers;

import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.dto.UserUpdateDto;
import com.pg.mbti.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/me")
@AllArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for managing authenticated user's profile")
public class ProfileController {

    private final UsersService usersService;

    @GetMapping
    @Operation(summary = "Get current user profile",
            description = "Retrieves the profile information of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserProfileDto> getUserProfile(
            @Parameter(description = "Authentication information of the current user")
            final Authentication authentication) {
        return ResponseEntity.ok(usersService.getUserProfileByNickname(authentication.getName()));
    }

    @PostMapping("photo/upload")
    @Operation(summary = "Upload profile photo",
            description = "Uploads a new profile photo for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo successfully uploaded",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Error storing file or internal server error")
    })
    public ResponseEntity<String> uploadProfilePhoto(
            @Parameter(description = "Authentication information of the current user")
            Authentication authentication,
            @Parameter(description = "Image file to upload (max 10MB)", required = true)
            @RequestParam("image") MultipartFile file) {
        String fileName = usersService.uploadProfilePhoto(authentication.getName(), file);
        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }

    @GetMapping("/photo")
    @Operation(summary = "Get current user profile photo",
            description = "Retrieves the profile photo of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile photo retrieved successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Profile photo not found"),
            @ApiResponse(responseCode = "500", description = "Error retrieving profile photo")
    })
    public ResponseEntity<Resource> getProfilePhoto(
            @Parameter(description = "Authentication information of the current user")
            Authentication authentication) {
        return ResponseEntity.ok(usersService.getProfilePhoto(authentication.getName()));
    }

    @PutMapping
    @Operation(summary = "Update current user profile",
            description = "Updates the profile information of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> updateUserProfile(
            @Parameter(description = "Authentication information of the current user")
            final Authentication authentication,
            @RequestBody UserUpdateDto userUpdateDto) {
        usersService.updateUserProfile(authentication.getName(), userUpdateDto);
        return ResponseEntity.ok("Profile successfully updated");
    }
}