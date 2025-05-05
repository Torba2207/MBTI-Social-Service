package com.pg.mbti.exception;

public class TagCategoryNotFoundException extends RuntimeException {
    public TagCategoryNotFoundException(String message) {
        super(message);
    }
}
