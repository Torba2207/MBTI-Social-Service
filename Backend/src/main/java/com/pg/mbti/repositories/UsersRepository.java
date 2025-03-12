package com.pg.mbti.repositories;

import com.pg.mbti.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);
}
