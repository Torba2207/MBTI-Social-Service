package com.pg.mbti.controllers;

import com.pg.mbti.entity.user.User;
import com.pg.mbti.services.UsersService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UsersController {

    UsersService usersService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return usersService.getUsers();
    }
}
