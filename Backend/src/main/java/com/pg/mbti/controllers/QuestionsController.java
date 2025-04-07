package com.pg.mbti.controllers;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.entity.questions.Answer;
import com.pg.mbti.entity.questions.Question;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.services.AnswerService;
import com.pg.mbti.services.QuestionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/questions")
@Tag(name = "Questions", description = "Endpoints for retrieving test questions")
public class QuestionsController {
    private final QuestionsService questionsService;
    private final AnswerService answerService;

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

    @PostMapping("/submit")
    @Operation(summary = "Send all user answers",
            description = "Send all user answers to the server with the specified MBTI type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answers successfully submitted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> submitAnswers(
            @RequestBody List<UserAnswerDto> answers,
            @Parameter(description = "MBTI type of the user", required = true)
            @RequestParam MBTIType mbtiType) {
        Answer processedAnswer = answerService.processUserAnswers(answers, mbtiType);
        return ResponseEntity.ok("Answers submitted successfully with ID: " + processedAnswer.getId());
    }
}