package com.pg.mbti.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/*
 * Enumerates the possible pronoun options for a user.
 * Each pronoun type has a corresponding display name.
 */
public enum Pronouns {
    HE_HIM("He/Him"),
    SHE_HER("She/Her"),
    THEY_THEM("They/Them"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String name;
}
