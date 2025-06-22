package com.pg.mbti.dto;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;

import java.util.Set;
import java.util.UUID;

/**
 * Represents criteria for searching users.
 * This record allows filtering and sorting users based on various attributes.
 */
public record UserSearchDto(
        /*
         * The name to filter users by.
         */
        String name,
        /*
         * The surname to filter users by.
         */
        String surname,
        /*
         * The MBTI type to filter users by.
         */
        MBTIType mbtiType,
        /*
         * The gender to filter users by.
         */
        Gender gender,
        /*
         * A set of tag IDs to filter users by.
         */
        Set<UUID> tagIds,
        /*
         * The field by which to sort the search results (e.g., "name", "gender, compatibility").
         */
        String sortBy,
        /*
         * The direction of sorting (e.g., "ASC" for ascending, "DESC" for descending).
         */
        String sortDirection,
        /*
         * A reference MBTI type, used for compatibility matching in search.
         */
        MBTIType referenceType
) {}