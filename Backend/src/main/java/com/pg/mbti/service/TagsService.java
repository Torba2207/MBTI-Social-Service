package com.pg.mbti.service;

import com.pg.mbti.model.Tag;
import com.pg.mbti.exception.TagCategoryNotFoundException;
import com.pg.mbti.repository.TagsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.List;

/**
 * Service class for managing tags and tag categories.
 */
@Service
@AllArgsConstructor
@Slf4j // Enable logging for this class
public class TagsService {
    private final TagsRepository tagsRepository;

    /**
     * Retrieves a list of all distinct tag categories.
     *
     * @return A list of strings, each representing a unique tag category.
     */
    public List<String> getAllTagCategories() {
        log.debug("Fetching all tag categories."); // Log fetching all tag categories
        return tagsRepository.findAllTagCategories();
    }

    /**
     * Retrieves a list of all tags.
     *
     * @return A list of all {@link Tag} entities.
     */
    public List<Tag> getAllTags() {
        log.debug("Fetching all tags."); // Log fetching all tags
        return tagsRepository.findAll();
    }

    /**
     * Retrieves a list of tags belonging to a specific category.
     *
     * @param category The name of the category to filter tags by.
     * @return A list of {@link Tag} entities within the specified category.
     * @throws TagCategoryNotFoundException If the specified category does not exist.
     */
    public List<Tag> getTagsByCategory(String category) {
        log.debug("Fetching tags by category: {}", category); // Log fetching tags by category
        if (!tagsRepository.findAllTagCategories().contains(category)) {
            log.warn("Tag category not found: {}", category); // Log category not found
            throw new TagCategoryNotFoundException("Category not found");
        }
        return tagsRepository.findAllByCategory(category);
    }

    /**
     * Creates a new tag.
     *
     * @param tag The {@link Tag} entity to create.
     * @return The saved {@link Tag} entity.
     * @throws IllegalArgumentException If a tag with the same name already exists.
     */
    public Tag createTag(Tag tag) {
        log.info("Attempting to create new tag: {}", tag.getName()); // Log tag creation attempt
        if (tagsRepository.existsByName(tag.getName())) {
            log.warn("Tag with name '{}' already exists.", tag.getName()); // Log existing tag
            throw new IllegalArgumentException("Tag with this name already exists");
        }
        Tag savedTag = tagsRepository.save(tag);
        log.info("Tag '{}' created successfully with ID: {}", savedTag.getName(), savedTag.getId()); // Log successful tag creation
        return savedTag;
    }
}