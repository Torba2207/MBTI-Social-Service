package com.pg.mbti.dto;

import lombok.Builder;

/**
 * Represents the context for sending an email.
 * This record contains the recipient, subject, and message body of the email.
 */
@Builder
public record EmailContextDto(
        /*
         * The email address of the recipient.
         */
        String recipient,
        /*
         * The subject line of the email.
         */
        String subject,
        /*
         * The main content or body of the email message.
         */
        String message
) {
}