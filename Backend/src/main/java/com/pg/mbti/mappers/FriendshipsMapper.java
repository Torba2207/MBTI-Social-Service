package com.pg.mbti.mappers;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.entity.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipsMapper {
    public FriendshipDto toFriendshipDto(final Friendship friendship) {
        return new FriendshipDto(friendship.getSenderId().getId(), friendship.getReceiverId().getId(),
                friendship.isPending(), friendship.getStartDate());
    }
}
