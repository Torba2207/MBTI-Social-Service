package com.pg.mbti.repositories;

import com.pg.mbti.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendship, UUID> {

    @Query("SELECT f FROM Friendship f WHERE f.receiverId.nickname = ?1 AND f.isPending = ?2")
    List<Friendship> findByUserNicknameAndIsPending(String name, boolean isPending);

    List<Friendship> findByUserNickname(String name);
}
