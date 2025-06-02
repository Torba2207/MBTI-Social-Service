package com.pg.mbti.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/*
 * Enumerates the 16 possible MBTI (Myers-Briggs Type Indicator) personality types.
 * Each MBTI type has a corresponding string representation.
 */
public enum MBTIType {
    ENFJ("ENFJ"),
    ENFP("ENFP"),
    ENTJ("ENTJ"),
    ENTP("ENTP"),
    ESFJ("ESFJ"),
    ESFP("ESFP"),
    ESTJ("ESTJ"),
    ESTP("ESTP"),
    INFJ("INFJ"),
    INFP("INFP"),
    INTJ("INTJ"),
    INTP("INTP"),
    ISFJ("ISFJ"),
    ISFP("ISFP"),
    ISTJ("ISTJ"),
    ISTP("ISTP");

    private final String name;
}
