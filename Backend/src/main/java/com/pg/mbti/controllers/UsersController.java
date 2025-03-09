package com.pg.mbti.controllers;

import com.pg.mbti.entity.user.User;
import com.pg.mbti.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UsersController {

    UsersService usersService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return usersService.getUsers();
    }

    @GetMapping("/users/{nickname}")
    public User getUserByNickname(@PathVariable final String nickname) {
        return usersService.getUserByNickname(nickname);
    }
}
