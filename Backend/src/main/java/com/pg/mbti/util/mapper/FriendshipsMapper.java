package com.pg.mbti.util.mapper;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.model.Friendship;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

/**
 * A utility class for mapping {@link Friendship} entities to {@link FriendshipDto} data transfer objects.
 * This class is marked as a Spring Component to be managed by the Spring IoC container.
 */
@Component
@Slf4j // Enable logging for this class
public class FriendshipsMapper {
    /**
     * Converts a {@link Friendship} entity to a {@link FriendshipDto}.
     *
     * @param friendship The {@link Friendship} entity to convert.
     * @return A new {@link FriendshipDto} containing the mapped data.
     */
    public static FriendshipDto toFriendshipDto(final Friendship friendship) {
        log.debug("Mapping Friendship entity with ID {} to FriendshipDto.", friendship.getId()); // Log the mapping process
        return new FriendshipDto(
                friendship.getSenderId().getNickname(), // Get the ID of the sender
                friendship.getReceiverId().getNickname(), // Get the ID of the receiver
                friendship.isPending(), // Get the pending status of the friendship
                friendship.getStartDate() // Get the start date of the friendship
        );
    }
}