package com.pg.mbti;

import com.pg.mbti.controllers.UserProfileController;
import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.dto.UserUpdateDto;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private Authentication authentication;

    @Mock
    private Resource resource;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserProfileController userProfileController;

    private UserProfileDto userProfileDto;

    @BeforeEach
    void setUp() {
        userProfileDto = new UserProfileDto(
                "user1@mbti.com",
                "user1",
                "John",
                "Doe",
                Date.valueOf("2000-01-01"),
                MBTIType.INTJ,
                null,
                null,
                Gender.MALE,
                "profile.jpg",
                "",
                Pronouns.HE_HIM,
                null,
                null
        );

        when(authentication.getName()).thenReturn("user1");
    }

    @Test
    void getUserProfileReturnsCurrentUserProfile() {
        when(usersService.getUserProfileByNickname("user1")).thenReturn(userProfileDto);

        ResponseEntity<UserProfileDto> response = userProfileController.getUserProfile(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userProfileDto);
        verify(usersService).getUserProfileByNickname("user1");
    }

    @Test
    void uploadProfilePhotoReturnsFileName() {
        String fileName = "new-profile.jpg";
        when(usersService.uploadProfilePhoto("user1", multipartFile)).thenReturn(fileName);

        ResponseEntity<String> response = userProfileController.uploadProfilePhoto(authentication, multipartFile);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("File uploaded successfully: " + fileName);
        verify(usersService).uploadProfilePhoto("user1", multipartFile);
    }

    @Test
    void getProfilePhotoReturnsUserProfilePhoto() {
        when(usersService.getProfilePhoto("user1")).thenReturn(resource);

        ResponseEntity<Resource> response = userProfileController.getProfilePhoto(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(resource);
        verify(usersService).getProfilePhoto("user1");
    }

    @Test
    void updateUserProfileCallsServiceWithCorrectParameters() {
        UserUpdateDto updateDto = new UserUpdateDto(
                50.0,
                50.0,
                "2000-02-02",
                Gender.PEDIK,
                "Updated description",
                Collections.singletonList("Updated links"),
                Pronouns.THEY_THEM,
                null
        );

        ResponseEntity<String> response = userProfileController.updateUserProfile(authentication, updateDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Profile successfully updated");
        verify(usersService).updateUserProfile("user1", updateDto);
    }
}
