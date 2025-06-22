package com.pg.mbti.dto.auth;

import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Pronouns;

import java.util.Date;

import com.pg.mbti.util.validator.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents a request for user registration.
 * This record contains all the necessary information to create a new user account.
 */
public record RegistrationRequestDto(
        /*
         * The name of the user. It cannot be blank.
         */
        @NotBlank(message = "Name is required")
        String name,

        /*
         * The surname of the user. It cannot be blank.
         */
        @NotBlank(message = "Surname is required")
        String surname,

        /*
         * The nickname of the user. It cannot be blank and must be between 3 and 20 characters.
         * It can only contain letters, numbers, and underscores.
         */
        @NotBlank(message = "Nickname is required")
        @Size(min = 3, max = 20, message = "Nickname must be between 3 and 20 characters")
        @Pattern(regexp = ValidationPatterns.NICKNAME_REGEX, message = "Nickname can only contain letters, numbers and underscores")
        String nickname,

        /*
         * The password for the user account. It cannot be blank, must be at least 8 characters long,
         * and must contain at least one digit, one lowercase letter, one uppercase letter, and one special character.
         */
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = ValidationPatterns.PASSWORD_REGEX,
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
        )
        String password,

        /*
         * The email address of the user. It must be a valid email format.
         */
        @Email(message = "Email should be valid")
        String email,

        /*
         * The latitude of the user's location.
         */
        Double latitude,
        /*
         * The longitude of the user's location.
         */
        Double longitude,

        /*
         * The MBTI (Myers-Briggs Type Indicator) type of the user.
         */
        MBTIType mbtiType,

        /*
         * The birthday of the user.
         */
        Date birthday,

        /*
         * The gender of the user.
         */
        Gender gender,
        /*
         * The pronouns preferred by the user.
         */
        Pronouns pronouns
) {}