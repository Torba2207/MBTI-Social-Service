package com.pg.mbti.controllers;

import com.pg.mbti.entity.User;
import com.pg.mbti.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UsersController {

    UsersService usersService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(usersService.getUsers());
    }

    @GetMapping("/users/{nickname}")
    public ResponseEntity<User> getUserByNickname(@PathVariable final String nickname) {
        return ResponseEntity.ok(usersService.getUserByNickname(nickname));
    }
}
