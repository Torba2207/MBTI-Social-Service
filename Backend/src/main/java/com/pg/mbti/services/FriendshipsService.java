package com.pg.mbti.services;

import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.entity.Friendship;
import com.pg.mbti.entity.User;
import com.pg.mbti.exceptions.FriendshipAlreadyExistsException;
import com.pg.mbti.exceptions.FriendshipNotFoundException;
import com.pg.mbti.util.mappers.FriendshipsMapper;
import com.pg.mbti.repositories.FriendshipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipsService {
    private final FriendshipsRepository friendshipRepository;
    private final UsersService usersService;

    public List<FriendshipDto> getAllFriendships() {
        return friendshipRepository.findAll().stream()
                .map(FriendshipsMapper::toFriendshipDto)
                .toList();
    }

    public List<FriendshipDto> getMyPendingFriendships(String name) {
        return friendshipRepository.getMyPendingFriendships(name).stream()
                .map(FriendshipsMapper::toFriendshipDto)
                .toList();
    }

    public List<FriendshipDto> getFriendshipsByNickname(String name) {
        return friendshipRepository.getFriendshipsByNickname(name).stream()
                .map(FriendshipsMapper::toFriendshipDto)
                .toList();
    }

    public void createFriendship(String currentNickname, String newFriendNickname) {
        User sender = usersService.getUserByNickname(currentNickname);
        User receiver = usersService.getUserByNickname(newFriendNickname);

        if (friendshipRepository.existsByFriends(currentNickname, newFriendNickname)) {
            throw new FriendshipAlreadyExistsException("Friendship already exists");
        }

        friendshipRepository.save(new Friendship(sender, receiver));
    }

    public void acceptFriendship(String currentNickname, String newFriendNickname) {
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));
        friendship.setPending(false);
        friendshipRepository.save(friendship);
    }

    public void deleteFriendship(String currentNickname, String newFriendNickname) {
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));

        friendshipRepository.delete(friendship);
    }
}