package com.pg.mbti.controllers;

import com.pg.mbti.entity.Tag;
import com.pg.mbti.services.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
