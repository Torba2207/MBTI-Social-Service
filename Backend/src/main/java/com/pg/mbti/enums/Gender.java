package com.pg.mbti.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/*
 * Enumerates the possible gender options for a user.
 * Each gender type has a corresponding display name.
 */
public enum  Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String name;
}
