package com.pg.mbti.repositories;

import com.pg.mbti.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagsRepository extends JpaRepository<Tag, UUID> {
    Set<Tag> findAllByIdIn(Set<UUID> tagIds);

    @Query("SELECT DISTINCT t.category FROM Tag t")
    List<String> findAllTagsCategories();
}
