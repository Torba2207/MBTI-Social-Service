package com.pg.mbti.dto;

import java.util.Date;
import java.util.UUID;

public record FriendshipDto(
        UUID senderId,
        UUID receiverId,
        boolean isPending,
        Date startDate
) {
}
