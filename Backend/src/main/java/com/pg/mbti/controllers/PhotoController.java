package com.pg.mbti.controllers;

import com.pg.mbti.services.PhotoService;
import com.pg.mbti.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photo")
@AllArgsConstructor
public class PhotoController {

    private final UsersService usersService;
    private final PhotoService photoService;

    @GetMapping("{nickname}")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable String nickname) {
        final var user = usersService.getUserByNickname(nickname);
        String fileName = user.getProfilePicture();
        return photoService.getProfilePhotoResponse(fileName);
    }
}
