package com.pg.mbti.service;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.model.Friendship;
import com.pg.mbti.model.User;
import com.pg.mbti.exception.FriendshipAlreadyExistsException;
import com.pg.mbti.exception.FriendshipNotFoundException;
import com.pg.mbti.util.mapper.FriendshipsMapper;
import com.pg.mbti.repository.FriendshipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.List;

/**
 * Service class for managing friendship relationships between users.
 */
@Service
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class FriendshipsService {
    private final FriendshipsRepository friendshipRepository;
    private final UsersService usersService;

    /**
     * Retrieves all friendship records from the database.
     *
     * @return A list of {@link FriendshipDto} representing all friendships.
     */
    public List<FriendshipDto> getAllFriendships() {
        log.debug("Fetching all friendships."); // Log fetching all friendships
        return friendshipRepository.findAll().stream()
                .map(FriendshipsMapper::toFriendshipDto)
                .toList();
    }

    /**
     * Retrieves all pending friendship requests where the specified user is the receiver.
     *
     * @param name The nickname of the user who is the receiver of the pending friendships.
     * @return A list of {@link FriendshipDto} representing pending requests.
     */
    public List<FriendshipDto> getMyPendingFriendships(String name) {
        log.debug("Fetching pending friendships for user: {}", name); // Log fetching pending friendships
        return friendshipRepository.getMyPendingFriendships(name).stream()
                .map(FriendshipsMapper::toFriendshipDto)
                .toList();
    }

    /**
     * Retrieves all accepted friendships (where {@code isPending} is false) involving a user with the given nickname.
     *
     * @param name The nickname of the user whose friendships are to be retrieved.
     * @return A list of {@link FriendshipDto} representing accepted friendships.
     */
    public List<FriendshipDto> getFriendshipsByNickname(String name) {
        log.debug("Fetching all accepted friendships for user: {}", name); // Log fetching accepted friendships
        return friendshipRepository.getFriendshipsByNickname(name).stream()
                .map(FriendshipsMapper::toFriendshipDto)
                .toList();
    }

    /**
     * Creates a new friendship request between two users.
     *
     * @param currentNickname The nickname of the user initiating the request (sender).
     * @param newFriendNickname The nickname of the user receiving the request (receiver).
     * @throws FriendshipAlreadyExistsException If a friendship (pending or accepted) already exists between the two users.
     */
    public void createFriendship(String currentNickname, String newFriendNickname) {
        log.info("Attempting to create friendship between {} and {}", currentNickname, newFriendNickname); // Log creation attempt
        User sender = usersService.getUserByNickname(currentNickname);
        User receiver = usersService.getUserByNickname(newFriendNickname);

        if (friendshipRepository.existsByFriends(currentNickname, newFriendNickname)) {
            log.warn("Friendship already exists between {} and {}", currentNickname, newFriendNickname); // Log existing friendship
            throw new FriendshipAlreadyExistsException("Friendship already exists");
        }

        friendshipRepository.save(new Friendship(sender, receiver));
        log.info("Friendship request created from {} to {}", currentNickname, newFriendNickname); // Log successful creation
    }

    /**
     * Accepts a pending friendship request between two users.
     *
     * @param currentNickname The nickname of one of the users involved in the friendship.
     * @param newFriendNickname The nickname of the other user involved in the friendship.
     * @throws FriendshipNotFoundException If the friendship request is not found.
     */
    public void acceptFriendship(String currentNickname, String newFriendNickname) {
        log.info("Attempting to accept friendship between {} and {}", currentNickname, newFriendNickname); // Log acceptance attempt
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname)
                .orElseThrow(() -> {
                    log.warn("Friendship not found to accept between {} and {}", currentNickname, newFriendNickname); // Log friendship not found
                    return new FriendshipNotFoundException("Friendship not found");
                });
        friendship.setPending(false);
        friendshipRepository.save(friendship);
        log.info("Friendship between {} and {} successfully accepted.", currentNickname, newFriendNickname); // Log successful acceptance
    }

    /**
     * Deletes a friendship between two users.
     * If the friendship does not exist, the method completes without action.
     *
     * @param currentNickname The nickname of one of the users involved in the friendship.
     * @param newFriendNickname The nickname of the other user involved in the friendship.
     */
    public void deleteFriendship(String currentNickname, String newFriendNickname) {
        log.info("Attempting to delete friendship between {} and {}", currentNickname, newFriendNickname); // Log deletion attempt
        friendshipRepository.findByFriends(currentNickname, newFriendNickname)
                .ifPresent(friendshipRepository::delete);
        log.info("Friendship successfully deleted between {} and {}", currentNickname, newFriendNickname); // Log successful deletion
    }
}