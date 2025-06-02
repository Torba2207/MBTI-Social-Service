package com.pg.mbti.repository;

import com.pg.mbti.model.User;
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

/**
 * Repository interface for managing {@link User} entities.
 * Provides standard CRUD operations and custom queries for user data.
 */
@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
    /**
     * Checks if a user with the given email address exists.
     *
     * @param email The email address to check.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    boolean existsByEmail(String email);
    /**
     * Checks if a user with the given nickname exists.
     *
     * @param nickname The nickname to check.
     * @return {@code true} if a user with the given nickname exists, {@code false} otherwise.
     */
    boolean existsByNickname(String nickname);

    /**
     * Finds a user by their nickname.
     *
     * @param nickname The nickname of the user to find.
     * @return An {@link Optional} containing the {@link User} if found, otherwise empty.
     */
    Optional<User> findByNickname(String nickname);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An {@link Optional} containing the {@link User} if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);

    /**
     * Updates the password of a user identified by their email address.
     * This operation is transactional and modifies the existing user record.
     *
     * @param email The email address of the user whose password is to be updated.
     * @param encode The new encoded password.
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = ?2 WHERE u.email = ?1")
    void updatePasswordByEmail(String email, String encode);

    /**
     * Finds users based on a combination of filters including name, surname, MBTI type, gender, and associated tags.
     * It excludes the authenticated user and anonymous roles.
     *
     * @param name The name to filter by (partial match using LIKE). Can be {@code null}.
     * @param surname The surname to filter by (partial match using LIKE). Can be {@code null}.
     * @param mbtiType The MBTI type to filter by. Can be {@code null}.
     * @param gender The gender to filter by. Can be {@code null}.
     * @param tagIds A set of tag IDs to filter by. Users must have at least one of these tags. Can be {@code null}.
     * @param authenticatedUsername The nickname of the currently authenticated user to exclude from results. Can be {@code null}.
     * @param sort The sorting criteria to apply to the results.
     * @return A {@link List} of {@link User} objects matching the specified filters.
     */
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.tags t WHERE " +
            "(:name IS NULL OR u.name LIKE :name) AND " +
            "(:authenticatedUsername IS NULL OR u.nickname != :authenticatedUsername) AND " +
            "(u.role != 'ANONYMOUS') AND " +
            "(:surname IS NULL OR u.surname LIKE :surname) AND " +
            "(:mbtiType IS NULL OR u.mbtiType = :mbtiType) AND " +
            "(:gender IS NULL OR u.gender = :gender) AND " +
            "(:tagIds IS NULL OR t.id IN :tagIds)")
    List<User> findUsersByFilters(
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("mbtiType") MBTIType mbtiType,
            @Param("gender") Gender gender,
            @Param("tagIds") Set<UUID> tagIds,
            @Param("authenticatedUsername") String authenticatedUsername,
            Sort sort);
}