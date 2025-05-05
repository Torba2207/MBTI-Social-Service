package com.pg.mbti.util.validator;

import com.pg.mbti.exception.InvalidEmailException;
import org.apache.commons.lang3.StringUtils;

public class EmailValidator {
    public static void validateEmailFormat(String email) {
        if (StringUtils.isBlank(email) || !email.matches(ValidationPatterns.EMAIL_REGEX)) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
