package com.pg.mbti.util.validator;

import com.pg.mbti.exception.InvalidEmailException;

public class EmailValidator {
    public static void validateEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
