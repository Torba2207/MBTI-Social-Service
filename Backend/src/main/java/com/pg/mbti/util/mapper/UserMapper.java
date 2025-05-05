package com.pg.mbti.util.mapper;

import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getSurname(),
                user.getBirthday(),
                user.getMbtiType(),
                user.getLatitude(),
                user.getLongitude(),
                user.getGender(),
                user.getProfilePicture(),
                user.getDescription(),
                user.getPronouns(),
                user.getLinks(),
                user.getTags());
    }
}