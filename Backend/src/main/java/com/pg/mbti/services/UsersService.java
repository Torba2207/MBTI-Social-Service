package com.pg.mbti.services;

import com.pg.mbti.entity.User;
import com.pg.mbti.repositories.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.GONE;

@Service
public class UsersService {

    UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    public User getUserByNickname(final String username) {
        return usersRepository.findByNickname(username)
                .orElseThrow(() -> new ResponseStatusException(GONE,
                        "The user account has been deleted or inactivated"));
    }
}
