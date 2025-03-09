package com.pg.mbti.services;

import com.pg.mbti.entity.User;
import com.pg.mbti.repositories.UsersRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User request) {
        if (userRepository.existsByNickname(request.getNickname()) ||
                userRepository.existsByEmail(request.getEmail())) {

            throw new ValidationException(
                    "Nickname or Email already exists");
        }

        User user = new User(request.getName(), request.getSurname(), request.getNickname(),
                passwordEncoder.encode(request.getPassword()), request.getEmail(),
                request.getLatitude(), request.getLongitude(), request.getMbtiType(),
                request.getAge(), request.getGender());

        return userRepository.save(user);
    }
}