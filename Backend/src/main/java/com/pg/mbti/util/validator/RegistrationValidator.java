package com.pg.mbti.util.validator;

import com.pg.mbti.dto.auth.RegistrationRequestDto;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class RegistrationValidator {

    private static final int MAX_AGE_YEARS = 120;

    public static void validate(RegistrationRequestDto request) {
        DtoValidator.validate(request);

        List<String> errors = new ArrayList<>();

        validateBirthday(request.birthday(), errors);
        validateCoordinates(request.latitude(), request.longitude(), errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Registration validation failed: " + String.join(", ", errors));
        }
    }

    private static void validateBirthday(Date birthday, List<String> errors) {
        if (birthday == null) return;

        LocalDate birthDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();

        if (birthDate.isAfter(now)) {
            errors.add("Birthday cannot be in the future");
        } else if (birthDate.plusYears(MAX_AGE_YEARS).isBefore(now)) {
            errors.add("Invalid birthday: age exceeds " + MAX_AGE_YEARS + " years");
        }
    }

    private static void validateCoordinates(Double latitude, Double longitude, List<String> errors) {
        if (latitude != null && (latitude < -90 || latitude > 90)) {
            errors.add("Latitude must be between -90 and 90");
        }

        if (longitude != null && (longitude < -180 || longitude > 180)) {
            errors.add("Longitude must be between -180 and 180");
        }
    }
}
