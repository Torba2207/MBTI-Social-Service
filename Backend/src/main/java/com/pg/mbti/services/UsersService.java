package com.pg.mbti.services;

import com.pg.mbti.entity.User;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersService {

    UsersRepository usersRepository;

    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    public User getUserByNickname(final String username) {
        return usersRepository.findByNickname(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void updateUser(User user) {
        usersRepository.save(user);
    }
}