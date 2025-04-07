package com.pg.mbti.mappers;

import com.pg.mbti.dto.RegistrationRequestDto;
import com.pg.mbti.dto.RegistrationResponseDto;
import com.pg.mbti.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {

    public User toEntity(RegistrationRequestDto registrationRequestDto) {
        return User.builder()
                .email(registrationRequestDto.email())
                .nickname(registrationRequestDto.nickname())
                .password(registrationRequestDto.password())
                .latitude(registrationRequestDto.latitude())
                .longitude(registrationRequestDto.longitude())
                .mbtiType(registrationRequestDto.mbti())
                .name(registrationRequestDto.name())
                .surname(registrationRequestDto.surname())
                .birthday(registrationRequestDto.birthday())
                .gender(registrationRequestDto.gender())
                .pronouns(registrationRequestDto.pronouns())
                .build();
    }

    public RegistrationResponseDto toRegistrationResponseDto(final User user) {
        return new RegistrationResponseDto(
                user.getEmail(),
                user.getNickname()
        );
    }
}