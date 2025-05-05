package com.pg.mbti.util.mapper;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.model.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipsMapper {
    public static FriendshipDto toFriendshipDto(final Friendship friendship) {
        return new FriendshipDto(
                friendship.getSenderId().getId(),
                friendship.getReceiverId().getId(),
                friendship.isPending(),
                friendship.getStartDate()
        );
    }
}
