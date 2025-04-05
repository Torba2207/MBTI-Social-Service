package com.pg.mbti;

import com.pg.mbti.dto.UserSearchDto;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.repositories.UsersRepository;
import com.pg.mbti.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    private List<User> mockUsers;
    private final UUID tag1Id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setName("John");
        user1.setSurname("Doe");
        user1.setMbtiType(MBTIType.INTJ);
        user1.setGender(Gender.MALE);

        User user2 = new User();
        user2.setName("Jane");
        user2.setSurname("Smith");
        user2.setMbtiType(MBTIType.ENFP);
        user2.setGender(Gender.FEMALE);

        mockUsers = Arrays.asList(user1, user2);
    }

    @Test
    void searchUsersWithNameParameter() {
        UserSearchDto searchDto = new UserSearchDto("John", null, null, null, null, null, null);

        when(usersRepository.findUsersByFilters(eq("John"), isNull(), isNull(), isNull(), isNull(), any(Sort.class)))
                .thenReturn(Collections.singletonList(mockUsers.getFirst()));

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getName());
        verify(usersRepository).findUsersByFilters(eq("John"), isNull(), isNull(), isNull(), isNull(), eq(Sort.unsorted()));
    }

    @Test
    void searchUsersWithMultipleParameters() {
        Set<UUID> tagIds = Collections.singleton(tag1Id);
        UserSearchDto searchDto = new UserSearchDto("Jane", "Smith", MBTIType.ENFP, Gender.FEMALE, tagIds, null, null);

        when(usersRepository.findUsersByFilters(eq("Jane"), eq("Smith"), eq(MBTIType.ENFP),
                eq(Gender.FEMALE), eq(tagIds), any(Sort.class)))
                .thenReturn(Collections.singletonList(mockUsers.get(1)));

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(1, result.size());
        assertEquals("Jane", result.getFirst().getName());
        assertEquals("Smith", result.getFirst().getSurname());
    }

    @Test
    void searchUsersWithSortingAscending() {
        UserSearchDto searchDto = new UserSearchDto(null, null, null, null, null, "name", "asc");

        when(usersRepository.findUsersByFilters(isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(Sort.by(Sort.Direction.ASC, "name"))))
                .thenReturn(mockUsers);

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(2, result.size());
        verify(usersRepository).findUsersByFilters(isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    void searchUsersWithSortingDescending() {
        UserSearchDto searchDto = new UserSearchDto(null, null, null, null, null, "surname", "desc");

        when(usersRepository.findUsersByFilters(isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(Sort.by(Sort.Direction.DESC, "surname"))))
                .thenReturn(mockUsers);

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(2, result.size());
        verify(usersRepository).findUsersByFilters(isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(Sort.by(Sort.Direction.DESC, "surname")));
    }

    @Test
    void searchUsersWithNoResults() {
        UserSearchDto searchDto = new UserSearchDto("NonExistent", null, null, null, null, null, null);

        when(usersRepository.findUsersByFilters(eq("NonExistent"), isNull(), isNull(), isNull(), isNull(), any(Sort.class)))
                .thenReturn(Collections.emptyList());

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(0, result.size());
    }

    @Test
    void searchUsersWithEmptySearchDto() {
        UserSearchDto searchDto = new UserSearchDto(null, null, null, null, null, null, null);

        when(usersRepository.findUsersByFilters(isNull(), isNull(), isNull(), isNull(), isNull(), eq(Sort.unsorted())))
                .thenReturn(mockUsers);

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(2, result.size());
    }

    @Test
    void searchUsersByMbtiType() {
        UserSearchDto searchDto = new UserSearchDto(null, null, MBTIType.INTJ, null, null, null, null);

        when(usersRepository.findUsersByFilters(isNull(), isNull(), eq(MBTIType.INTJ), isNull(), isNull(), any(Sort.class)))
                .thenReturn(Collections.singletonList(mockUsers.getFirst()));

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(1, result.size());
        assertEquals(MBTIType.INTJ, result.getFirst().getMbtiType());
    }

    @Test
    void searchUsersByGender() {
        UserSearchDto searchDto = new UserSearchDto(null, null, null, Gender.FEMALE, null, null, null);

        when(usersRepository.findUsersByFilters(isNull(), isNull(), isNull(), eq(Gender.FEMALE), isNull(), any(Sort.class)))
                .thenReturn(Collections.singletonList(mockUsers.get(1)));

        List<User> result = usersService.searchUsers(searchDto);

        assertEquals(1, result.size());
        assertEquals(Gender.FEMALE, result.getFirst().getGender());
    }
}
