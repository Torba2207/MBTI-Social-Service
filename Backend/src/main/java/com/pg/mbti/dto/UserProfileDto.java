package com.pg.mbti.dto;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;

public record UserProfileDto(
        String email,
        String nickname,
        String name,
        String surname,
        Integer age,
        MBTIType mbtiType,
        Double latitude,
        Double longitude,
        Gender gender,
        String profilePictureUrl
) {
}
