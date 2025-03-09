package com.pg.mbti.mappers;

import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(user.getEmail(), user.getNickname());
    }
}