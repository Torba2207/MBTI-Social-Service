package com.pg.mbti.exceptions;

public class EmailSendingFailedException extends RuntimeException {
    public EmailSendingFailedException(String message) {
        super(message);
    }
}
