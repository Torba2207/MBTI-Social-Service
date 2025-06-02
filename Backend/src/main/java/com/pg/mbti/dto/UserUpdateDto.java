package com.pg.mbti.dto;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Pronouns;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a request to update a user's profile information.
 * This record contains fields that a user is allowed to modify on their profile.
 */
@Builder
public record UserUpdateDto(
        /*
         * The updated latitude coordinate of the user's location.
         */
        Double latitude,
        /*
         * The updated longitude coordinate of the user's location.
         */
        Double longitude,
        /*
         * The updated birthday of the user, as a string.
         */
        String birthday,
        /*
         * The updated gender of the user.
         */
        Gender gender,
        /*
         * The updated textual description for the user's profile.
         */
        String description,
        /*
         * The updated list of external links associated with the user's profile.
         */
        List<String> links,
        /*
         * The updated preferred pronouns of the user.
         */
        Pronouns pronouns,
        /*
         * The updated set of tag IDs associated with the user's profile.
         */
        Set<UUID> tagIds
) {
}