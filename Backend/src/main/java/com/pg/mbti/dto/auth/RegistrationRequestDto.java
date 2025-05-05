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

public record RegistrationRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Surname is required")
        String surname,

        @NotBlank(message = "Nickname is required")
        @Size(min = 3, max = 20, message = "Nickname must be between 3 and 20 characters")
        @Pattern(regexp = ValidationPatterns.NICKNAME_REGEX, message = "Nickname can only contain letters, numbers and underscores")
        String nickname,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = ValidationPatterns.PASSWORD_REGEX,
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
        )
        String password,

        @Email(message = "Email should be valid")
        String email,

        Double latitude,
        Double longitude,

        MBTIType mbtiType,

        Date birthday,

        Gender gender,
        Pronouns pronouns
) {}
