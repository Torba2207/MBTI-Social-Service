package com.pg.mbti.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Pronouns {
    HE_HIM("He/Him"),
    SHE_HER("She/Her"),
    THEY_THEM("They/Them"),
    OTHER("Other"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String name;
}
