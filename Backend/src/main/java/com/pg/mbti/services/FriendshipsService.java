package com.pg.mbti.services;

import com.pg.mbti.entity.Friendship;
import com.pg.mbti.entity.User;
import com.pg.mbti.exceptions.FriendshipAlreadyExistsException;
import com.pg.mbti.exceptions.FriendshipNotFoundException;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.repositories.FriendshipsRepository;
import com.pg.mbti.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipsService {
    FriendshipsRepository friendshipRepository;
    UsersRepository usersRepository;

    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    public List<Friendship> getMyPendingFriendships(String name) {
        return friendshipRepository.getMyPendingFriendships(name);
    }

    public List<Friendship> getFriendshipsByNickname(String name) {
        return friendshipRepository.getFriendshipsByNickname(name);
    }

    private User findUserByNickname(String nickname) {
        return usersRepository
                .findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + nickname));
    }

    public void createFriendship(String currentNickname, String newFriendNickname) {
        User sender = findUserByNickname(currentNickname);
        User receiver = findUserByNickname(newFriendNickname);

        if (friendshipRepository.existsByFriends(currentNickname, newFriendNickname)) {
            throw new FriendshipAlreadyExistsException("Friendship already exists");
        }

        friendshipRepository.save(new Friendship(sender, receiver));
    }

    public void acceptFriendship(String currentNickname, String newFriendNickname) {
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname);

        if (friendship == null) {
            throw new FriendshipNotFoundException("Friendship request not found");
        }

        friendship.setPending(false);
        friendshipRepository.save(friendship);
    }

    public void deleteFriendship(String currentNickname, String newFriendNickname) {
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname);

        if (friendship == null) {
            throw new FriendshipNotFoundException("Friendship not found");
        }

        friendshipRepository.delete(friendship);
    }
}