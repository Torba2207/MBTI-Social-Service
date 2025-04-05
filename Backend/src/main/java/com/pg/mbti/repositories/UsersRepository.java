package com.pg.mbti.repositories;

import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.tags t WHERE " +
            "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:surname IS NULL OR LOWER(u.surname) LIKE LOWER(CONCAT('%', :surname, '%'))) AND " +
            "(:mbtiType IS NULL OR u.mbtiType = :mbtiType) AND " +
            "(:gender IS NULL OR u.gender = :gender) AND " +
            "(:tagIds IS NULL OR t.id IN :tagIds)")
    List<User> findUsersByFilters(
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("mbtiType") MBTIType mbtiType,
            @Param("gender") Gender gender,
            @Param("tagIds") Set<UUID> tagIds,
            Sort sort);
}
