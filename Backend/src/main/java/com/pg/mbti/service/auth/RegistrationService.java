package com.pg.mbti.service.auth;

import com.pg.mbti.dto.EmailContextDto;
import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.model.User;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exception.EmailSendingFailedException;
import com.pg.mbti.exception.InvalidTokenException;
import com.pg.mbti.exception.ResourceNotFoundException;
import com.pg.mbti.exception.UserAlreadyExistsException;
import com.pg.mbti.repository.UsersRepository;
import com.pg.mbti.service.EmailService;
import com.pg.mbti.util.SecureTokenUtil;
import com.pg.mbti.util.validator.EmailValidator;
import com.pg.mbti.util.validator.RegistrationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service class responsible for handling user registration and email confirmation.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SecureTokenUtil secureTokenUtil;

    @Value("${app.email.confirm-email-url}")
    private String confirmEmailUrl;

    @Value("${image.default.path}")
    private String defaultProfilePicture;

    /**
     * Registers a new user based on the provided registration request.
     * Performs validation, checks for existing users, creates the user, saves it, and sends a confirmation email.
     *
     * @param registrationRequestDto The DTO containing the user's registration details.
     * @throws UserAlreadyExistsException If a user with the same nickname or email already exists.
     * @throws EmailSendingFailedException If there is an issue sending the confirmation email.
     */
    @Transactional
    public void registerUser(RegistrationRequestDto registrationRequestDto) {
        log.info("Starting registration process for email: {}", registrationRequestDto.email()); // Log start of registration
        RegistrationValidator.validate(registrationRequestDto);
        EmailValidator.validateEmailFormat(registrationRequestDto.email());
        log.debug("Registration request validated for: {}", registrationRequestDto.email()); // Log validation success
        checkUserExists(registrationRequestDto);

        User user = createUser(registrationRequestDto);
        userRepository.save(user);
        log.info("User registered with email: {}. Sending confirmation email.", user.getEmail()); // Log user saved and email send initiation
        sendConfirmationEmail(user);
    }

    /**
     * Checks if a user with the provided nickname or email already exists in the repository.
     *
     * @param dto The registration request DTO containing nickname and email.
     * @throws UserAlreadyExistsException If a user with the same nickname or email is found.
     */
    private void checkUserExists(RegistrationRequestDto dto) {
        if (userRepository.existsByNickname(dto.nickname()) ||
                userRepository.existsByEmail(dto.email())) {
            log.warn("Registration failed: User already exists with nickname '{}' or email '{}'.", dto.nickname(), dto.email()); // Log existing user
            throw new UserAlreadyExistsException("Nickname or Email already exists");
        }
        log.debug("No existing user found with nickname '{}' or email '{}'.", dto.nickname(), dto.email()); // Log no existing user
    }

    /**
     * Creates a new User entity from the registration request DTO.
     * Encodes the password and sets the initial role to ANONYMOUS.
     *
     * @param dto The registration request DTO.
     * @return A new User entity.
     */
    private User createUser(RegistrationRequestDto dto) {
        log.debug("Creating user entity for email: {}", dto.email()); // Log user entity creation
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
                .profilePicture(defaultProfilePicture)
                .mbtiType(dto.mbtiType())
                .gender(dto.gender())
                .pronouns(dto.pronouns())
                .build();
    }

    /**
     * Sends a confirmation email to the newly registered user.
     * The email contains a link with a secure token for email verification.
     *
     * @param user The User for whom to send the confirmation email.
     * @throws EmailSendingFailedException If there is an error during email sending.
     * @throws InvalidTokenException If a secure token cannot be generated.
     */
    private void sendConfirmationEmail(User user) {
        try {
            log.debug("Generating confirmation token for user: {}", user.getEmail()); // Log token generation
            String token = secureTokenUtil.generateToken(user.getId().toString(), 1, TimeUnit.DAYS)
                    .orElseThrow(() -> {
                        log.error("Failed to generate confirmation token for user: {}", user.getEmail()); // Log token generation failure
                        return new InvalidTokenException("Invalid token");
                    });
            String confirmationLink = confirmEmailUrl + token;
            EmailContextDto emailContext = EmailContextDto.builder()
                    .recipient(user.getEmail())
                    .subject("Email Confirmation")
                    .message("Click the link to confirm your email: " + confirmationLink)
                    .build();
            emailService.sendMail(emailContext);
            log.info("Confirmation email successfully sent to: {}", user.getEmail()); // Log successful email send
        } catch (Exception e) {
            log.error("Failed to send confirmation email to {}. Error: {}", user.getEmail(), e.getMessage()); // Log email sending failure
            throw new EmailSendingFailedException("Failed to send confirmation email: " + e.getMessage());
        }
    }

    /**
     * Confirms a user's email address using a provided token.
     * If the token is valid, the user's role is updated to VERIFIED.
     *
     * @param token The secure token received by the user via email.
     * @throws InvalidTokenException If the token is invalid or expired.
     * @throws ResourceNotFoundException If the user associated with the token is not found.
     */
    public void confirmEmail(String token) {
        log.info("Attempting to confirm email with token: {}", token); // Log email confirmation attempt

        String userId = secureTokenUtil.getValue(token)
                .orElseThrow(() -> {
                    log.warn("Email confirmation failed: Invalid or expired token '{}'.", token); // Log invalid token
                    return new InvalidTokenException("Invalid or expired token");
                });

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> {
                    log.warn("Email confirmation failed: User not found for ID '{}'.", userId); // Log user not found
                    return new ResourceNotFoundException("User not found");
                });
        user.setRole(Role.VERIFIED);
        userRepository.save(user);
        secureTokenUtil.deleteValue(token);
        log.info("User with ID '{}' has been successfully verified.", userId); // Log successful verification
    }
}