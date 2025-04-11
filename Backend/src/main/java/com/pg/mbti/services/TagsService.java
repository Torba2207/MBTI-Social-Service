package com.pg.mbti.services;

import com.pg.mbti.entity.Tag;
import com.pg.mbti.repositories.TagsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagsService {
    private final TagsRepository tagsRepository;

    public List<String> getAllTagsCategories() {
        return tagsRepository.findAllTagsCategories();
    }

    public List<Tag> getAllTags() {
        return tagsRepository.findAll();
    }
}
