package com.pg.mbti.util.validator;

import com.pg.mbti.dto.auth.RegistrationRequestDto;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Utility class for performing comprehensive validation on {@link RegistrationRequestDto}.
 * It includes validation for DTO constraints, birthday, and geographical coordinates.
 */
@Slf4j
public class RegistrationValidator {

    private static final int MAX_AGE_YEARS = 120; // Maximum allowed age for registration

    /**
     * Validates the provided {@link RegistrationRequestDto}.
     * It first performs standard DTO validation, then additional checks for birthday and coordinates.
     *
     * @param request The {@link RegistrationRequestDto} to validate.
     * @throws IllegalArgumentException If any validation rule is violated, with a concatenated error message.
     */
    public static void validate(RegistrationRequestDto request) {
        log.debug("Starting registration validation for: {}", request.email()); // Log the start of registration validation
        DtoValidator.validate(request); // Perform general DTO validation

        List<String> errors = new ArrayList<>();

        validateBirthday(request.birthday(), errors); // Validate the user's birthday
        validateCoordinates(request.latitude(), request.longitude(), errors); // Validate geographical coordinates

        if (!errors.isEmpty()) {
            log.warn("Registration validation failed for {}: {}", request.email(), String.join(", ", errors)); // Log registration validation failure
            throw new IllegalArgumentException("Registration validation failed: " + String.join(", ", errors));
        }
        log.debug("Registration validation passed for: {}", request.email()); // Log successful registration validation
    }

    /**
     * Validates the user's birthday.
     * Checks if the birthday is in the future or implies an age exceeding {@link #MAX_AGE_YEARS}.
     *
     * @param birthday The birthday {@link Date} to validate.
     * @param errors A list to which validation error messages will be added.
     */
    private static void validateBirthday(Date birthday, List<String> errors) {
        if (birthday == null) {
            log.debug("Birthday is null, skipping birthday validation"); // Log if birthday is null
            return;
        }

        LocalDate birthDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();

        if (birthDate.isAfter(now)) {
            log.debug("Birthday {} is in the future", birthDate); // Log birthday in the future
            errors.add("Birthday cannot be in the future");
        } else if (birthDate.plusYears(MAX_AGE_YEARS).isBefore(now)) {
            log.debug("Birthday {} exceeds max age of {}", birthDate, MAX_AGE_YEARS); // Log birthday exceeding max age
            errors.add("Invalid birthday: age exceeds " + MAX_AGE_YEARS + " years");
        }
    }

    /**
     * Validates the provided latitude and longitude coordinates.
     * Checks if the latitude is within the range [-90, 90] and longitude within [-180, 180].
     *
     * @param latitude The latitude value to validate.
     * @param longitude The longitude value to validate.
     * @param errors A list to which validation error messages will be added.
     */
    private static void validateCoordinates(Double latitude, Double longitude, List<String> errors) {
        if (latitude != null && (latitude < -90 || latitude > 90)) {
            log.debug("Latitude {} is out of range", latitude); // Log latitude out of range
            errors.add("Latitude must be between -90 and 90");
        }

        if (longitude != null && (longitude < -180 || longitude > 180)) {
            log.debug("Longitude {} is out of range", longitude); // Log longitude out of range
            errors.add("Longitude must be between -180 and 180");
        }
    }
}