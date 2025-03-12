package com.pg.mbti.dto;

import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Gender;

public record RegistrationRequestDto(
        String name,
        String surname,
        String nickname,
        String password,
        String email,
        Double latitude,
        Double longitude,
        MBTIType mbti,
        Integer age,
        Gender gender
    ) { }