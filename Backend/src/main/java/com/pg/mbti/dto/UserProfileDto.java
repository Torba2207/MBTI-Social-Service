package com.pg.mbti.dto;

import com.pg.mbti.model.Tag;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Represents a comprehensive profile of a user.
 * This record includes various personal details, preferences, and social information.
 */
@Builder
public record UserProfileDto(
        /*
         * The email address of the user.
         */
        String email,
        /*
         * The nickname of the user.
         */
        String nickname,
        /*
         * The first name of the user.
         */
        String name,
        /*
         * The surname of the user.
         */
        String surname,
        /*
         * The birthday of the user.
         */
        Date birthday,
        /*
         * The MBTI (Myers-Briggs Type Indicator) type of the user.
         */
        MBTIType mbtiType,
        /*
         * The latitude coordinate of the user's location.
         */
        Double latitude,
        /*
         * The longitude coordinate of the user's location.
         */
        Double longitude,
        /*
         * The gender of the user.
         */
        Gender gender,
        /*
         * The URL to the user's profile picture.
         */
        String profilePictureUrl,
        /*
         * A textual description provided by the user for their profile.
         */
        String description,
        /*
         * The preferred pronouns of the user.
         */
        Pronouns pronouns,
        /*
         * A list of external links associated with the user's profile.
         */
        List<String> links,
        /*
         * A set of tags associated with the user's profile.
         */
        Set<Tag> tags
) {
}