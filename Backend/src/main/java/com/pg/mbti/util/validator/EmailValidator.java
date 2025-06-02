package com.pg.mbti.util.validator;

import com.pg.mbti.exception.InvalidEmailException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for validating email formats.
 */
@Slf4j
public class EmailValidator {
    /**
     * Validates the format of an email string against a predefined regex pattern.
     *
     * @param email The email string to validate.
     * @throws InvalidEmailException If the email is blank or does not match the valid email format.
     */
    public static void validateEmailFormat(String email) {
        log.debug("Validating email format for: {}", email); // Log the email validation attempt
        if (StringUtils.isBlank(email) || !email.matches(ValidationPatterns.EMAIL_REGEX)) {
            log.warn("Invalid email format: {}", email); // Log invalid email format
            throw new InvalidEmailException("Invalid email format");
        }
        log.debug("Email format is valid for: {}", email); // Log successful email format validation
    }
}