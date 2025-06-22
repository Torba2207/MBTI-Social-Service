package com.pg.mbti.repository;

import com.pg.mbti.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Repository interface for managing {@link Tag} entities.
 * Provides standard CRUD operations and custom queries for tags.
 */
@Repository
public interface TagsRepository extends JpaRepository<Tag, UUID> {
    /**
     * Finds all tags whose IDs are present in the given set of tag IDs.
     *
     * @param tagIds A set of UUIDs representing the tag IDs to search for.
     * @return A {@link Set} of {@link Tag} objects matching the provided IDs.
     */
    Set<Tag> findAllByIdIn(Set<UUID> tagIds);

    /**
     * Retrieves all distinct tag categories.
     *
     * @return A {@link List} of strings, where each string is a unique tag category.
     */
    @Query("SELECT DISTINCT t.category FROM Tag t")
    List<String> findAllTagCategories();

    /**
     * Finds all tags belonging to a specific category.
     *
     * @param category The category name to search for.
     * @return A {@link List} of {@link Tag} objects that belong to the specified category.
     */
    @Query("SELECT t FROM Tag t WHERE t.category = :category")
    List<Tag> findAllByCategory(String category);

    /**
     * Checks if a tag with the given name already exists.
     *
     * @param name The name of the tag to check.
     * @return {@code true} if a tag with the given name exists, {@code false} otherwise.
     */
    boolean existsByName(String name);
}