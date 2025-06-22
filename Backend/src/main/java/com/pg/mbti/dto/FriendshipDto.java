package com.pg.mbti.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Represents the details of a friendship relationship between two users.
 * This record includes information about the sender, receiver, status, and start date of the friendship.
 */
public record FriendshipDto(
        /*
         * The unique username of the user who initiated the friendship request.
         */
        String senderNickname,
        /*
         * The unique username of the user who is the recipient of the friendship request.
         */
        String receiverNickname,
        /*
         * A boolean indicating whether the friendship request is still pending (true) or has been accepted (false).
         */
        boolean isPending,
        /*
         * The date when the friendship was initiated or became active.
         */
        Date startDate
) {
}