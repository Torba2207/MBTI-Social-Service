package com.pg.mbti.util.validator;

/**
 * A utility class holding regular expression patterns for common validation scenarios.
 */
public class ValidationPatterns {
    /**
     * Regex for validating passwords.
     * Requires at least one digit, one lowercase letter, one uppercase letter, and one special character.
     */
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$";
    /**
     * Regex for validating nicknames.
     * Allows letters, numbers, and underscores, with a length between 3 and 20 characters.
     */
    public static final String NICKNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    /**
     * Regex for validating email addresses.
     * A common pattern to ensure a valid email structure.
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
}