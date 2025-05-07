package com.pg.mbti.controller;

import com.pg.mbti.model.questions.Question;
import com.pg.mbti.service.QuestionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/questions")
@Tag(name = "Questions", description = "Endpoints for manage test questions")
public class QuestionsController {
    private final QuestionsService questionsService;

    @GetMapping
    @Operation(summary = "Get all questions",
            description = "Returns a list of all test questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of questions successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionsService.getAllQuestions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID",
            description = "Returns a question with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question successfully found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Question> getQuestionById(
            @Parameter(description = "Unique identifier of the question", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(questionsService.getQuestionById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new question",
            description = "Creates a new question with the specified details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Question> createQuestion(
            @Parameter(description = "Details of the question to be created", required = true)
            @RequestBody Question question) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(questionsService.createQuestion(question));
    }
}