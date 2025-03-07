package com.pg.mbti.entity.user;

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
