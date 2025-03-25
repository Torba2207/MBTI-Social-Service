package com.pg.mbti.dto;

import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Pronouns;

import java.util.Date;

public record RegistrationRequestDto(
        String name,
        String surname,
        String nickname,
        String password,
        String email,
        Double latitude,
        Double longitude,
        MBTIType mbti,
        Date birthday,
        Gender gender,
        Pronouns pronouns
) {
}