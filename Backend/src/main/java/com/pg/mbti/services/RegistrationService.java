package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.UserAlreadyExistsException;
import com.pg.mbti.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureTokenService secureTokenService;

    @Transactional
    public void registerUser(User request) {
        if (userRepository.existsByNickname(request.getNickname()) ||
                userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Nickname or Email already exists");
        }

        User user = new User(request.getName(), request.getSurname(), request.getNickname(),
                passwordEncoder.encode(request.getPassword()), request.getEmail(),
                request.getLatitude(), request.getLongitude(), request.getMbtiType(),
                request.getAge(), request.getGender());

        userRepository.save(user);

        try {
            String token = secureTokenService.generateToken(user.getId().toString(), 1, TimeUnit.DAYS);
            String confirmationLink = "http://localhost:8080/api/auth/confirm-email?token=" + token;
            EmailContextDto emailContext = EmailContextDto.builder()
                    .recipient(user.getEmail())
                    .subject("Email Confirmation")
                    .message("Click the link to confirm your email: " + confirmationLink)
                    .build();
            emailService.sendMail(emailContext);
        } catch (Exception e) {
            throw new EmailSendingFailedException("Failed to send confirmation email: " + e.getMessage());
        }
    }

    public void confirmEmail(String token) {
        String userId = secureTokenService.getValue(token);
        if (userId == null) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(Role.VERIFIED);
        userRepository.save(user);
        secureTokenService.deleteValue(token);
    }
}