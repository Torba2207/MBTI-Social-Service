package com.pg.mbti.exceptions;

public class TagCategoryNotFoundException extends RuntimeException {
    public TagCategoryNotFoundException(String message) {
        super(message);
    }
}
