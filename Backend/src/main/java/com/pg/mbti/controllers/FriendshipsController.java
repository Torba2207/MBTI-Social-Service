package com.pg.mbti.controllers;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.mappers.FriendshipsMapper;
import com.pg.mbti.services.FriendshipsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friendships")
@AllArgsConstructor
public class FriendshipsController {
    FriendshipsService friendshipService;
    FriendshipsMapper friendshipsMapper;

    @GetMapping
    public List<FriendshipDto> getFriendships() {
        return friendshipService.getAllFriendships()
                .stream()
                .map(friendship -> friendshipsMapper.toFriendshipDto(friendship))
                .collect(Collectors.toList());
    }

    @GetMapping("/me/pending")
    public ResponseEntity<List<FriendshipDto>> getFriendshipsByNickname(
            final Authentication authentication
    ) {
        final var friendships = friendshipService.getMyPendingFriendships(
                authentication.getName())
                .stream()
                .map(friendship -> friendshipsMapper.toFriendshipDto(friendship))
                .collect(Collectors.toList());;
        return ResponseEntity.ok(friendships);
    }

    @GetMapping("/me/accepted")
    public ResponseEntity<List<FriendshipDto>> getFriendshipsByNickname(final Authentication authentication) {
        final var friendships = friendshipService.getFriendshipsByNickname(
                        authentication.getName())
                .stream()
                .map(friendship -> friendshipsMapper.toFriendshipDto(friendship))
                .collect(Collectors.toList());;
        return ResponseEntity.ok(friendships);
    }

    @PostMapping("/me/{receiver}")
    public ResponseEntity<Integer> createFriendship(
            final Authentication authentication,
            @PathVariable final String receiver
    ) {
        friendshipService.createFriendship(
                authentication.getName(), receiver);
        return ResponseEntity.ok(200);
    }

    @GetMapping("/me/{receiver}/accept")
    public ResponseEntity<Integer> updateFriendship(
            final Authentication authentication,
            @PathVariable final String receiver
    ) {
        friendshipService.acceptFriendship(
                authentication.getName(), receiver);
        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/me/{receiver}")
    public ResponseEntity<Integer> deleteFriendship(
            final Authentication authentication,
            @PathVariable final String receiver
    ) {
        friendshipService.deleteFriendship(
                authentication.getName(), receiver);
        return ResponseEntity.ok(200);
    }
}
