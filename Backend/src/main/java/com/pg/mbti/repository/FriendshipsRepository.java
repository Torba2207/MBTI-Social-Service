package com.pg.mbti.repository;

import com.pg.mbti.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Friendship} entities.
 * Provides standard CRUD operations and custom queries related to friendships.
 */
@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, UUID> {

    /**
     * Retrieves a list of pending friendship requests where the specified user is the receiver.
     *
     * @param name The nickname of the user who is the receiver of the pending friendships.
     * @return A list of {@link Friendship} objects representing pending requests.
     */
    @Query("SELECT f FROM Friendship f " +
            "WHERE f.receiverId.nickname = ?1 " +
            "AND f.isPending = true")
    List<Friendship> getMyPendingFriendships(String name);

    /**
     * Retrieves all friendships (both sent and received, if not pending) involving a user with the given nickname.
     *
     * @param name The nickname of the user whose friendships are to be retrieved.
     * @return A list of {@link Friendship} objects.
     */
    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.receiverId.nickname = ?1 " +
            "OR f.senderId.nickname = ?1) " +
            "AND f.isPending = false")
    List<Friendship> getFriendshipsByNickname(String name);

    /**
     * Finds a friendship between two users identified by their nicknames, regardless of who is the sender or receiver.
     *
     * @param currentNickname The nickname of the first user.
     * @param newFriendNickname The nickname of the second user.
     * @return An {@link Optional} containing the {@link Friendship} if found, otherwise empty.
     */
    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.receiverId.nickname = ?1 " +
            "AND f.senderId.nickname = ?2) " +
            "OR (f.receiverId.nickname = ?2 " +
            "AND f.senderId.nickname = ?1)")
    Optional<Friendship> findByFriends(String currentNickname, String newFriendNickname);

    /**
     * Checks if a friendship already exists between two users identified by their nicknames.
     *
     * @param currentNickname The nickname of the first user.
     * @param newFriendNickname The nickname of the second user.
     * @return {@code true} if a friendship exists, {@code false} otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friendship f " +
            "WHERE (f.receiverId.nickname = ?1 " +
            "AND f.senderId.nickname = ?2) " +
            "OR (f.receiverId.nickname = ?2 " +
            "AND f.senderId.nickname = ?1)")
    boolean existsByFriends(String currentNickname, String newFriendNickname);
}