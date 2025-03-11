package com.pg.mbti.services;

import com.pg.mbti.entity.Friendship;
import com.pg.mbti.repositories.FriendshipsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipsService {
    FriendshipsRepository friendshipRepository;

    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    public List<Friendship> getFriendshipsByNicknameAndIsPending(String name, boolean isPending) {
        return friendshipRepository.findByUserNicknameAndIsPending(name, isPending);
    }

    public List<Friendship> getFriendshipsByNickname(String name) {
        return friendshipRepository.findByUserNickname(name);
    }

    public void updateFriendship(String name, String receiver) {
        // TODO: Implement this method
    }
}
