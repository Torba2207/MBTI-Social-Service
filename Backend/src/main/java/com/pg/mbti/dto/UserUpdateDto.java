package com.pg.mbti.dto;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Pronouns;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserUpdateDto(
        Double latitude,
        Double longitude,
        String birthday,
        Gender gender,
        String description,
        List<String> links,
        Pronouns pronouns,
        Set<UUID> tagIds
) {
}
