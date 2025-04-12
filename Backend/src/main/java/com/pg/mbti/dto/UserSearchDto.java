package com.pg.mbti.dto;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;

import java.util.Set;
import java.util.UUID;

public record UserSearchDto(
        String name,
        String surname,
        MBTIType mbtiType,
        Gender gender,
        Set<UUID> tagIds,
        String sortBy,
        String sortDirection,
        MBTIType referenceType
) {}