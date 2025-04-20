package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.UserAlreadyExistsException;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pg.mbti.services.email.EmailValidator;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureTokenService secureTokenService;

    @Value("${app.email.confirm-email-url}")
    private String confirmEmailUrl;

    @Value("${image.default.path}")
    private String defaultImagePath;

    @Transactional
    public void registerUser(User request) {
        EmailValidator.validateEmailFormat(request.getEmail());

        if (userRepository.existsByNickname(request.getNickname()) ||
                userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Nickname or Email already exists");
        }

        User user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ANONYMOUS)
                .birthday(request.getBirthday())
                .name(request.getName())
                .surname(request.getSurname())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .profilePicture(defaultImagePath)
                .mbtiType(request.getMbtiType())
                .gender(request.getGender())
                .pronouns(request.getPronouns())
                .build();

        userRepository.save(user);

        try {
            int tokenExpirationTime = 1;
            String token = secureTokenService.generateToken(user.getId().toString(), tokenExpirationTime, TimeUnit.DAYS);
            String confirmationLink = confirmEmailUrl + token;
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