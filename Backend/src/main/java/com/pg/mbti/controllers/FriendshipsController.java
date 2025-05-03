
package com.pg.mbti.controllers;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.services.FriendshipsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
@Tag(name = "Friendship Management", description = "Endpoints for managing user friendships")
public class FriendshipsController {
    private final FriendshipsService friendshipService;

    @GetMapping
    @Operation(summary = "Get all friendships", description = "Retrieves all friendships in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all friendships",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FriendshipDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<FriendshipDto> getFriendships() {
        return friendshipService.getAllFriendships();
    }

    @GetMapping("/me/pending")
    @Operation(summary = "Get pending friendships",
            description = "Retrieves all pending friendship requests for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of pending friendships",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FriendshipDto.class))))
    })
    public ResponseEntity<List<FriendshipDto>> getPendingFriendshipsByNickname(
            @Parameter(description = "Authentication information of the current user") final Authentication authentication
    ) {
        return ResponseEntity.ok(friendshipService.getMyPendingFriendships(authentication.getName()));
    }

    @GetMapping("/me/accepted")
    @Operation(summary = "Get accepted friendships",
            description = "Retrieves all accepted friendships for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of accepted friendships",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FriendshipDto.class))))
    })
    public ResponseEntity<List<FriendshipDto>> getMyFriendships(
            @Parameter(description = "Authentication information of the current user") final Authentication authentication
    ) {
        return ResponseEntity.ok(friendshipService.getFriendshipsByNickname(authentication.getName()));
    }

    @PostMapping("/me/{receiver}")
    @Operation(summary = "Send friendship request",
            description = "Sends a friendship request from the authenticated user to a specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friendship request successfully sent",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    public ResponseEntity<String> createFriendship(
            @Parameter(description = "Authentication information of the current user") final Authentication authentication,
            @Parameter(description = "Nickname of the friendship request receiver") @PathVariable final String receiver
    ) {
        friendshipService.createFriendship(authentication.getName(), receiver);
        return ResponseEntity.ok("Friendship request sent");
    }

    @GetMapping("/me/{receiver}/accept")
    @Operation(summary = "Accept friendship request",
            description = "Accepts a pending friendship request from the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friendship successfully accepted",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    public ResponseEntity<String> updateFriendship(
            @Parameter(description = "Authentication information of the current user") final Authentication authentication,
            @Parameter(description = "Nickname of the friendship request sender") @PathVariable final String receiver
    ) {
        friendshipService.acceptFriendship(authentication.getName(), receiver);
        return ResponseEntity.ok("Friendship accepted");
    }

    @DeleteMapping("/me/{receiver}")
    @Operation(summary = "Delete friendship",
            description = "Deletes an existing friendship or rejects a pending friendship request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friendship successfully deleted",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    })
    public ResponseEntity<String> deleteFriendship(
            @Parameter(description = "Authentication information of the current user") final Authentication authentication,
            @Parameter(description = "Nickname of the other user in the friendship") @PathVariable final String receiver
    ) {
        friendshipService.deleteFriendship(authentication.getName(), receiver);
        return ResponseEntity.ok("Friendship deleted");
    }
}