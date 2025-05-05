package com.pg.mbti.repository;

import com.pg.mbti.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, UUID> {

    @Query("SELECT f FROM Friendship f " +
            "WHERE f.receiverId.nickname = ?1 " +
            "AND f.isPending = true")
    List<Friendship> getMyPendingFriendships(String name);

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.receiverId.nickname = ?1 " +
            "OR f.senderId.nickname = ?1) " +
            "AND f.isPending = false")
    List<Friendship> getFriendshipsByNickname(String name);

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.receiverId.nickname = ?1 " +
            "AND f.senderId.nickname = ?2) " +
            "OR (f.receiverId.nickname = ?2 " +
            "AND f.senderId.nickname = ?1)")
    Optional<Friendship> findByFriends(String currentNickname, String newFriendNickname);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Friendship f " +
            "WHERE (f.receiverId.nickname = ?1 " +
            "AND f.senderId.nickname = ?2) " +
            "OR (f.receiverId.nickname = ?2 " +
            "AND f.senderId.nickname = ?1)")
    boolean existsByFriends(String currentNickname, String newFriendNickname);
}
