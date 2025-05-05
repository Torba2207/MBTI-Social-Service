package com.pg.mbti.dto;

import com.pg.mbti.model.Tag;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
public record UserProfileDto(
        String email,
        String nickname,
        String name,
        String surname,
        Date birthday,
        MBTIType mbtiType,
        Double latitude,
        Double longitude,
        Gender gender,
        String profilePictureUrl,
        String description,
        Pronouns pronouns,
        List<String> links,
        Set<Tag> tags
) {
}
