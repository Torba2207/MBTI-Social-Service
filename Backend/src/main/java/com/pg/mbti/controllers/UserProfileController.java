package com.pg.mbti.controllers;

import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.mappers.UserMapper;
import com.pg.mbti.services.PhotoService;
import com.pg.mbti.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/me")
@AllArgsConstructor
public class UserProfileController {

    private final UsersService usersService;
    private final UserMapper userMapper;
    private PhotoService photoService;

    @GetMapping
    public ResponseEntity<UserProfileDto> getUserProfile(final Authentication authentication) {
        final var user = usersService.getUserByNickname(authentication.getName());
        return ResponseEntity.ok(userMapper.toUserProfileDto(user));
    }

    @PostMapping("photo/upload")
    public ResponseEntity<String> uploadProfilePhoto(Authentication authentication, @RequestParam("image") MultipartFile file) {
        String fileName = photoService.uploadPhoto(file);
        final var user = usersService.getUserByNickname(authentication.getName());
        user.setProfilePicture(fileName);
        usersService.updateUser(user);
        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }

    @GetMapping("/photo")
    public ResponseEntity<Resource> getProfilePhoto(Authentication authentication) {
        final var user = usersService.getUserByNickname(authentication.getName());
        String fileName = user.getProfilePicture();
        return photoService.getProfilePhotoResponse(fileName);
    }
}