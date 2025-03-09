package com.pg.mbti.dto;

import com.pg.mbti.entity.MBTIType;
import com.pg.mbti.entity.user.Gender;

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