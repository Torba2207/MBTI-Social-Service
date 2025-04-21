package com.pg.mbti.controllers;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.entity.questions.Answer;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.services.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;

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

    @GetMapping
    @Operation(summary = "Get all answers",
            description = "Retrieve all answers from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Answer.class))),
            @ApiResponse(responseCode = "404", description = "No answers found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Answer>> getAllAnswers() {
        List<Answer> answers = answerService.getAllAnswers();
        return ResponseEntity.ok(answers);
    }
}
