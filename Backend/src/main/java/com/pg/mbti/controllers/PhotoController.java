package com.pg.mbti.controllers;

import com.pg.mbti.services.PhotoService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photo")
@AllArgsConstructor
@Tag(name = "Profile Photos", description = "Endpoints for retrieving user profile photos")
public class PhotoController {

    private final UsersService usersService;
    private final PhotoService photoService;

    @GetMapping("{nickname}")
    @Operation(summary = "Get user profile photo",
            description = "Retrieves the profile photo of a user by their nickname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile photo retrieved successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "User not found or profile photo not available",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error retrieving profile photo",
                    content = @Content)
    })
    public ResponseEntity<Resource> getProfilePhoto(
            @Parameter(description = "Nickname of the user whose profile photo is requested", required = true)
            @PathVariable String nickname) {
        final var user = usersService.getUserByNickname(nickname);
        String fileName = user.getProfilePicture();
        return photoService.getProfilePhotoResponse(fileName);
    }

    @PostMapping
    @Operation(summary = "Upload photo",
            description = "Uploads a new photo to the server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo successfully uploaded",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Error storing file or internal server error")
    })
    public ResponseEntity<String> uploadProfilePhoto(
            @Parameter(description = "Image file to upload (max 10MB)", required = true)
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(photoService.uploadPhoto(file) + " uploaded successfully");
    }

    @GetMapping("/{fileName}")
    @Operation(summary = "Get photo by file name",
            description = "Retrieves a photo by its file name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photo retrieved successfully",
                    content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Photo not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error retrieving photo",
                    content = @Content)
    })
    public ResponseEntity<Resource> getPhotoByFileName(
            @Parameter(description = "File name of the photo to retrieve", required = true)
            @PathVariable String fileName) {
        return photoService.getProfilePhotoResponse(fileName);
    }
}