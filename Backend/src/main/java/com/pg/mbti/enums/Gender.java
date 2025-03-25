package com.pg.mbti.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  Gender {
    MALE("Male"),
    FEMALE("Female"),
    PEDIK("Pedik");

    private final String name;
}
