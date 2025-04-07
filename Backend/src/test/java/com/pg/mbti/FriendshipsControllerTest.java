package com.pg.mbti;

import com.pg.mbti.controllers.FriendshipsController;
import com.pg.mbti.dto.FriendshipDto;
import com.pg.mbti.entity.Friendship;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.FriendshipAlreadyExistsException;
import com.pg.mbti.exceptions.FriendshipNotFoundException;
import com.pg.mbti.mappers.FriendshipsMapper;
import com.pg.mbti.services.FriendshipsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendshipsControllerTest {

    @Mock
    private FriendshipsService friendshipsService;

    @Mock
    private FriendshipsMapper friendshipsMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FriendshipsController friendshipsController;

    private Friendship friendship;
    private FriendshipDto friendshipDto;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .nickname("user1")
                .name("user1")
                .surname("user1")
                .password("password")
                .email("user1@mbti.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();

        User user2 = User.builder()
                .nickname("user2")
                .name("user2")
                .surname("user2")
                .password("password")
                .email("user2@mbti.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();
        friendship = new Friendship(user1, user2);
        friendshipDto = new FriendshipDto(user1.getId(), user2.getId(), false, Date.valueOf("2000-01-01"));

        lenient().when(authentication.getName()).thenReturn("user1");
    }

    @Test
    void getAllFriendshipsReturnsAllFriendshipsFromService() {
        List<Friendship> friendships = Collections.singletonList(friendship);

        when(friendshipsService.getAllFriendships()).thenReturn(friendships);
        when(friendshipsMapper.toFriendshipDto(any(Friendship.class))).thenReturn(friendshipDto);

        List<FriendshipDto> result = friendshipsController.getFriendships();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(friendshipDto);
        verify(friendshipsService).getAllFriendships();
    }

    @Test
    void getPendingFriendshipsByNicknameReturnsPendingFriendships() {
        List<Friendship> pendingFriendships = Collections.singletonList(friendship);

        when(friendshipsService.getMyPendingFriendships("user1")).thenReturn(pendingFriendships);
        when(friendshipsMapper.toFriendshipDto(any(Friendship.class))).thenReturn(friendshipDto);

        ResponseEntity<List<FriendshipDto>> response = friendshipsController.getPendingFriendshipsByNickname(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst()).isEqualTo(friendshipDto);
        verify(friendshipsService).getMyPendingFriendships("user1");
    }

    @Test
    void getMyFriendshipsReturnsAcceptedFriendships() {
        List<Friendship> acceptedFriendships = Collections.singletonList(friendship);

        when(friendshipsService.getFriendshipsByNickname("user1")).thenReturn(acceptedFriendships);
        when(friendshipsMapper.toFriendshipDto(any(Friendship.class))).thenReturn(friendshipDto);

        ResponseEntity<List<FriendshipDto>> response = friendshipsController.getMyFriendships(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst()).isEqualTo(friendshipDto);
        verify(friendshipsService).getFriendshipsByNickname("user1");
    }

    @Test
    void createFriendshipReturnsSuccessWhenFriendshipCreated() {
        doNothing().when(friendshipsService).createFriendship("user1", "user2");

        ResponseEntity<String> response = friendshipsController.createFriendship(authentication, "user2");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Friendship request sent");
        verify(friendshipsService).createFriendship("user1", "user2");
    }

    @Test
    void createFriendshipThrowsExceptionWhenFriendshipAlreadyExists() {
        doThrow(new FriendshipAlreadyExistsException("Friendship already exists"))
                .when(friendshipsService).createFriendship("user1", "user2");

        assertThatThrownBy(() -> friendshipsController.createFriendship(authentication, "user2"))
                .isInstanceOf(FriendshipAlreadyExistsException.class)
                .hasMessage("Friendship already exists");

        verify(friendshipsService).createFriendship("user1", "user2");
    }

    @Test
    void updateFriendshipReturnsSuccessWhenFriendshipAccepted() {
        doNothing().when(friendshipsService).acceptFriendship("user1", "user2");

        ResponseEntity<String> response = friendshipsController.updateFriendship(authentication, "user2");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Friendship accepted");
        verify(friendshipsService).acceptFriendship("user1", "user2");
    }

    @Test
    void updateFriendshipThrowsExceptionWhenFriendshipNotFound() {
        doThrow(new FriendshipNotFoundException("Friendship request not found"))
                .when(friendshipsService).acceptFriendship("user1", "user2");

        assertThatThrownBy(() -> friendshipsController.updateFriendship(authentication, "user2"))
                .isInstanceOf(FriendshipNotFoundException.class)
                .hasMessage("Friendship request not found");

        verify(friendshipsService).acceptFriendship("user1", "user2");
    }

    @Test
    void deleteFriendshipReturnsSuccessWhenFriendshipDeleted() {
        doNothing().when(friendshipsService).deleteFriendship("user1", "user2");

        ResponseEntity<String> response = friendshipsController.deleteFriendship(authentication, "user2");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Friendship deleted");
        verify(friendshipsService).deleteFriendship("user1", "user2");
    }

    @Test
    void deleteFriendshipThrowsExceptionWhenFriendshipNotFound() {
        doThrow(new FriendshipNotFoundException("Friendship not found"))
                .when(friendshipsService).deleteFriendship("user1", "user2");

        assertThatThrownBy(() -> friendshipsController.deleteFriendship(authentication, "user2"))
                .isInstanceOf(FriendshipNotFoundException.class)
                .hasMessage("Friendship not found");

        verify(friendshipsService).deleteFriendship("user1", "user2");
    }
}