package com.pg.mbti.util.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Utility class for performing bean validation on DTOs using Jakarta Bean Validation.
 * It leverages {@link Validator} to check constraints defined on DTO fields.
 */
@Slf4j
public class DtoValidator {

    /**
     * Validates the given DTO object against its defined constraints.
     * If validation fails, an {@link IllegalArgumentException} is thrown with a descriptive message.
     *
     * @param <T> The type of the DTO being validated.
     * @param dto The DTO object to validate.
     * @throws IllegalArgumentException If the DTO fails validation due to constraint violations.
     */
    public static <T> void validate(T dto) {
        log.debug("Starting validation for DTO: {}", dto.getClass().getSimpleName()); // Log the start of DTO validation
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse("Invalid input");
                log.warn("Validation failed for DTO: {}. Errors: {}", dto.getClass().getSimpleName(), errorMsg); // Log validation failure
                throw new IllegalArgumentException("Validation failed: " + errorMsg);
            }
            log.debug("Validation passed for DTO: {}", dto.getClass().getSimpleName()); // Log validation success
        }
    }
}