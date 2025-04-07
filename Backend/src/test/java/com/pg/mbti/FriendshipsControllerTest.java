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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private MockMvc mockMvc;
    private Friendship friendship;
    private FriendshipDto friendshipDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendshipsController)
                .build();

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

        when(authentication.getName()).thenReturn("user1");
    }

    @Test
    void getFriendships_ShouldReturnAllFriendships() throws Exception {
        List<Friendship> friendships = Collections.singletonList(friendship);

        when(friendshipsService.getAllFriendships()).thenReturn(friendships);
        when(friendshipsMapper.toFriendshipDto(any(Friendship.class))).thenReturn(friendshipDto);

        mockMvc.perform(get("/api/friendships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sender").value("user1"))
                .andExpect(jsonPath("$[0].receiver").value("user2"));

        verify(friendshipsService).getAllFriendships();
    }

    @Test
    void getPendingFriendshipsByNickname_ShouldReturnPendingFriendships() throws Exception {
        List<Friendship> pendingFriendships = Collections.singletonList(friendship);

        when(friendshipsService.getMyPendingFriendships("user1")).thenReturn(pendingFriendships);
        when(friendshipsMapper.toFriendshipDto(any(Friendship.class))).thenReturn(friendshipDto);

        mockMvc.perform(get("/api/friendships/me/pending")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sender").value("user1"));

        verify(friendshipsService).getMyPendingFriendships("user1");
    }

    @Test
    void getMyFriendships_ShouldReturnAcceptedFriendships() throws Exception {
        List<Friendship> acceptedFriendships = Collections.singletonList(friendship);

        when(friendshipsService.getFriendshipsByNickname("user1")).thenReturn(acceptedFriendships);
        when(friendshipsMapper.toFriendshipDto(any(Friendship.class))).thenReturn(friendshipDto);

        mockMvc.perform(get("/api/friendships/me/accepted")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sender").value("user1"));

        verify(friendshipsService).getFriendshipsByNickname("user1");
    }

    @Test
    void createFriendship_ShouldReturnSuccess() throws Exception {
        doNothing().when(friendshipsService).createFriendship("user1", "user2");

        mockMvc.perform(post("/api/friendships/me/user2")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Friendship request sent"));

        verify(friendshipsService).createFriendship("user1", "user2");
    }

    @Test
    void createFriendship_WhenFriendshipAlreadyExists() throws Exception {
        doThrow(new FriendshipAlreadyExistsException("Friendship already exists"))
                .when(friendshipsService).createFriendship("user1", "user2");

        mockMvc.perform(post("/api/friendships/me/user2")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(friendshipsService).createFriendship("user1", "user2");
    }

    @Test
    void updateFriendship_ShouldReturnSuccess() throws Exception {
        doNothing().when(friendshipsService).acceptFriendship("user1", "user2");

        mockMvc.perform(get("/api/friendships/me/user2/accept")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Friendship accepted"));

        verify(friendshipsService).acceptFriendship("user1", "user2");
    }

    @Test
    void updateFriendship_WhenFriendshipNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new FriendshipNotFoundException("Friendship request not found"))
                .when(friendshipsService).acceptFriendship("user1", "user2");

        mockMvc.perform(get("/api/friendships/me/user2/accept")
                        .principal(authentication))
                .andExpect(status().isNotFound());

        verify(friendshipsService).acceptFriendship("user1", "user2");
    }

    @Test
    void deleteFriendship_ShouldReturnSuccess() throws Exception {
        doNothing().when(friendshipsService).deleteFriendship("user1", "user2");

        mockMvc.perform(delete("/api/friendships/me/user2")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Friendship deleted"));

        verify(friendshipsService).deleteFriendship("user1", "user2");
    }

    @Test
    void deleteFriendship_WhenFriendshipNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new FriendshipNotFoundException("Friendship not found"))
                .when(friendshipsService).deleteFriendship("user1", "user2");

        mockMvc.perform(delete("/api/friendships/me/user2")
                        .principal(authentication))
                .andExpect(status().isNotFound());

        verify(friendshipsService).deleteFriendship("user1", "user2");
    }
}