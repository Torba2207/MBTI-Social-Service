package com.pg.mbti.services;

import com.pg.mbti.entity.Tag;
import com.pg.mbti.exceptions.TagCategoryNotFoundException;
import com.pg.mbti.repositories.TagsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagsService {
    private final TagsRepository tagsRepository;

    public List<String> getAllTagCategories() {
        return tagsRepository.findAllTagCategories();
    }

    public List<Tag> getAllTags() {
        return tagsRepository.findAll();
    }

    public List<Tag> getTagsByCategory(String category) {
        if (!tagsRepository.findAllTagCategories().contains(category)) {
            throw new TagCategoryNotFoundException("Category not found");
        }
        return tagsRepository.findAllByCategory(category);
    }

    public Tag createTag(Tag tag) {
        if (tagsRepository.existsByName(tag.getName())) {
            throw new IllegalArgumentException("Tag with this name already exists");
        }
        return tagsRepository.save(tag);
    }
}
