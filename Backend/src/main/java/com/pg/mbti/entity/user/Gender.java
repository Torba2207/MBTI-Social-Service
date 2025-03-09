package com.pg.mbti.entity.user;

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
