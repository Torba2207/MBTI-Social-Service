package com.pg.mbti.controllers;

import com.pg.mbti.dto.UserAnswerDto;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.services.MbtiPredictionService;
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

@RestController
@AllArgsConstructor
@RequestMapping("/api/mbti")
@Tag(name = "MBTI Prediction", description = "Endpoints for MBTI personality prediction")
public class MbtiPredictionController {

    private final MbtiPredictionService mbtiPredictionService;

    @PostMapping("/train")
    @Operation(summary = "Train MBTI prediction model",
            description = "Trains the custom decision tree with a specified depth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model successfully trained",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> trainModel(
            @Parameter(description = "Depth of the decision tree", required = true)
            @RequestParam int depth) {
        String result = mbtiPredictionService.trainModel(depth);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/questions")
    @Operation(summary = "Get MBTI questions",
            description = "Retrieves all questions used in the MBTI test")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions successfully retrieved",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<String>> getQuestions() {
        List<String> questions = mbtiPredictionService.getQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/predict")
    @Operation(summary = "Predict MBTI personality type",
            description = "Predicts MBTI personality type based on user answers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prediction successfully made",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MBTIType.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MBTIType> predictPersonality(
            @Parameter(description = "User answers to MBTI questions", required = true)
            @RequestBody List<UserAnswerDto> answers) {
        MBTIType prediction = mbtiPredictionService.predictPersonality(answers);
        return ResponseEntity.ok(prediction);
    }
}