package com.pg.mbti.util;

public class ValidationPatterns {
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$";
    public static final String NICKNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
}
