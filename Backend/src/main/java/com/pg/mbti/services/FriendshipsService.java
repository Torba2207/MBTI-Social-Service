package com.pg.mbti.services;

import com.pg.mbti.entity.Friendship;
import com.pg.mbti.entity.User;
import com.pg.mbti.repositories.FriendshipsRepository;
import com.pg.mbti.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipsService {
    FriendshipsRepository friendshipRepository;
    UsersRepository  usersRepository;

    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    public List<Friendship> getMyPendingFriendships(String name) {
        return friendshipRepository.getMyPendingFriendships(name);
    }

    public List<Friendship> getFriendshipsByNickname(String name) {
        return friendshipRepository.getFriendshipsByNickname(name);
    }

    private User findUserByNickname(String Nickname) {
        return usersRepository
                .findByNickname(Nickname)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void createFriendship(String currentNickname, String newFriendNickname) {
        User sender = findUserByNickname(currentNickname);
        User receiver = findUserByNickname(newFriendNickname);
        friendshipRepository.save(new Friendship(sender, receiver));
    }

    public void acceptFriendship(String currentNickname, String newFriendNickname) {
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname);
        friendship.setPending(false);
        friendshipRepository.save(friendship);
    }

    public void deleteFriendship(String currentNickname, String newFriendNickname) {
        Friendship friendship = friendshipRepository.findByFriends(currentNickname, newFriendNickname);
        friendshipRepository.delete(friendship);
    }
}
