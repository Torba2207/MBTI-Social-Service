package com.pg.mbti.repositories;

import com.pg.mbti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.password = ?2 " +
            "WHERE u.email = ?1")
    void updatePasswordByEmail(String email, String encode);
}
