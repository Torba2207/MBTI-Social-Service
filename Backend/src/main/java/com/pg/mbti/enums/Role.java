package com.pg.mbti.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ANONYMOUS("Anonymous"),
    VERIFIED("Verified"),
    ADMIN("Admin");

    private final String name;


}
