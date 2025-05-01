package com.pg.mbti.services;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.EmailSendingFailedException;
import com.pg.mbti.exceptions.InvalidTokenException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.exceptions.UserAlreadyExistsException;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.util.EmailValidator;
import com.pg.mbti.util.RegistrationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void registerUser(RegistrationRequestDto registrationRequestDto) {
        RegistrationValidator.validate(registrationRequestDto);
        EmailValidator.validateEmailFormat(registrationRequestDto.email());
        checkUserExists(registrationRequestDto);

        User user = createUser(registrationRequestDto);
        userRepository.save(user);
        sendConfirmationEmail(user);
    }

    private void checkUserExists(RegistrationRequestDto dto) {
        if (userRepository.existsByNickname(dto.nickname()) ||
                userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("Nickname or Email already exists");
        }
    }

    private User createUser(RegistrationRequestDto dto) {
        return User.builder()
                .nickname(dto.nickname())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.ANONYMOUS)
                .birthday(dto.birthday())
                .name(dto.name())
                .surname(dto.surname())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .profilePicture("default.jpg")
                .mbtiType(dto.mbtiType())
                .gender(dto.gender())
                .pronouns(dto.pronouns())
                .build();
    }

    private void sendConfirmationEmail(User user) {
        try {
            String token = secureTokenService.generateToken(user.getId().toString(), 1, TimeUnit.DAYS);
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
        String userId = secureTokenService.getValue(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired token"));

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(Role.VERIFIED);
        userRepository.save(user);
        secureTokenService.deleteValue(token);
    }
}