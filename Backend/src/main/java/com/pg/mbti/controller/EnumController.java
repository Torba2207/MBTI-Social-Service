package com.pg.mbti.controller;

import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.service.EnumsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/enums")
@Tag(name = "Enums Management", description = "Endpoints for getting enums present in service")
public class EnumController {

    private final EnumsService enumsService;

    @GetMapping("/roles")
    @Operation(summary = "Get user roles", description = "Fetch all roles available for user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of user roles",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Role.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<Role>> getAllUserRoles() {
        return ResponseEntity.ok(enumsService.getAllUserRoles());
    }

    @GetMapping("/genders")
    @Operation(summary = "Get user genders", description = "Fetch all genders available for user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of genders",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Gender.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<Gender>> getAllUserGenders() {
        return ResponseEntity.ok(enumsService.getAllUserGenders());
    }

    @GetMapping("/mbti")
    @Operation(summary = "Get MBTI types", description = "Fetch all MBTI personality types available in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of MBTI types",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MBTIType.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<MBTIType>> getAllMBTITypes() {
        return ResponseEntity.ok(enumsService.getAllMBTITypes());
    }

    @GetMapping("/pronouns")
    @Operation(summary = "Get pronouns", description = "Fetch all pronouns available for user profile selection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of pronouns",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pronouns.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<Pronouns>> getAllPronouns() {
        return ResponseEntity.ok(enumsService.getAllPronouns());
    }
}