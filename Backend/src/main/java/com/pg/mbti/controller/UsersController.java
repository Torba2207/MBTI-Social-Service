package com.pg.mbti.controller;

import com.pg.mbti.dto.UserSearchDto;
import com.pg.mbti.model.User;
import com.pg.mbti.service.UsersService;
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
@RequestMapping("/api")
@Tag(name = "Users", description = "Endpoints for retrieving user information")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/users")
    @Operation(summary = "Get all users",
            description = "Returns a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(usersService.getUsers());
    }

    @GetMapping("/users/{nickname}")
    @Operation(summary = "Get user by nickname",
            description = "Returns a user with the specified nickname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<User> getUserByNickname(
            @Parameter(description = "Nickname of the user to retrieve", required = true)
            @PathVariable final String nickname) {
        return ResponseEntity.ok(usersService.getUserByNickname(nickname));
    }

    @PostMapping("/users/search")
    @Operation(summary = "Search users",
            description = "Search users by name, surname, MBTI type, gender, and tags with sorting options")
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    public ResponseEntity<List<User>> searchUsers(@RequestBody UserSearchDto searchDto) {
        return ResponseEntity.ok(usersService.searchUsers(searchDto));
    }
}