package com.pg.mbti.controller;

import com.pg.mbti.model.Tag;
import com.pg.mbti.service.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags", description = "Endpoints for retrieving tags information")
public class TagsController {
    private final TagsService tagsService;

    @GetMapping("/categories")
    @Operation(summary = "Get all tag categories",
            description = "Returns a list of all tag categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tag categories successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<String>> getAllTagCategories() {
        return ResponseEntity.ok(tagsService.getAllTagCategories());
    }

    @GetMapping
    @Operation(summary = "Get all tags",
            description = "Returns a list of all tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tags successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(tagsService.getAllTags());
    }

    @GetMapping("{category}")
    @Operation(summary = "Get tags by category",
            description = "Returns a list of tags by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tags by category successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Tag>> getTagsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(tagsService.getTagsByCategory(category));
    }
    
    @PostMapping
    @Operation(summary = "Create a new tag",
            description = "Creates a new tag with the specified name and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tag.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tag data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(tagsService.createTag(tag));
    }
}
