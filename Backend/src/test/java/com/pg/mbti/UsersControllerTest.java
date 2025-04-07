package com.pg.mbti;

import com.pg.mbti.controllers.UsersController;
import com.pg.mbti.dto.UserSearchDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    private User user1;
    private User user2;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .nickname("user1")
                .name("John")
                .surname("Doe")
                .password("password")
                .email("user1@mbti.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();

        user2 = User.builder()
                .nickname("user2")
                .name("Jane")
                .surname("Smith")
                .password("password")
                .email("user2@mbti.com")
                .mbtiType(MBTIType.ENFP)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.FEMALE)
                .pronouns(Pronouns.SHE_HER)
                .role(Role.VERIFIED)
                .build();

        userList = Arrays.asList(user1, user2);
    }

    @Test
    void getUsersReturnsAllUsersFromService() {
        when(usersService.getUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = usersController.getUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0)).isEqualTo(user1);
        assertThat(response.getBody().get(1)).isEqualTo(user2);
        verify(usersService).getUsers();
    }

    @Test
    void getUserByNicknameReturnsUserWhenFound() {
        when(usersService.getUserByNickname("user1")).thenReturn(user1);

        ResponseEntity<User> response = usersController.getUserByNickname("user1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user1);
        verify(usersService).getUserByNickname("user1");
    }

    @Test
    void getUserByNicknameThrowsExceptionWhenUserNotFound() {
        when(usersService.getUserByNickname("nonexistent")).thenThrow(new ResourceNotFoundException("User not found"));

        assertThatThrownBy(() -> usersController.getUserByNickname("nonexistent"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(usersService).getUserByNickname("nonexistent");
    }

    @Test
    void searchUsersReturnsMatchingUsers() {
        UserSearchDto searchDto = new UserSearchDto("Jane", null, null, null, null, null, null);
        when(usersService.searchUsers(searchDto)).thenReturn(Collections.singletonList(user2));

        ResponseEntity<List<User>> response = usersController.searchUsers(searchDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst()).isEqualTo(user2);
        verify(usersService).searchUsers(searchDto);
    }

    @Test
    void searchUsersWithMultipleFilters() {
        UserSearchDto searchDto = new UserSearchDto(null, null, MBTIType.INTJ, Gender.MALE, null, "name", "asc");
        when(usersService.searchUsers(searchDto)).thenReturn(Collections.singletonList(user1));

        ResponseEntity<List<User>> response = usersController.searchUsers(searchDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst()).isEqualTo(user1);
        verify(usersService).searchUsers(searchDto);
    }

    @Test
    void searchUsersReturnsEmptyListWhenNoMatches() {
        UserSearchDto searchDto = new UserSearchDto("NonExistent", null, null, null, null, null, null);
        when(usersService.searchUsers(searchDto)).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = usersController.searchUsers(searchDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
        verify(usersService).searchUsers(searchDto);
    }
}