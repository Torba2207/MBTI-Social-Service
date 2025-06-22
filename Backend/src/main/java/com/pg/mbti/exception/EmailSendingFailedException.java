package com.pg.mbti.exception;

public class EmailSendingFailedException extends RuntimeException {
    public EmailSendingFailedException(String message) {
        super(message);
    }
}
