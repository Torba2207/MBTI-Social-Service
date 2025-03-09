package com.pg.mbti.mappers;

import com.pg.mbti.dto.RegistrationRequestDto;
import com.pg.mbti.dto.RegistrationResponseDto;
import com.pg.mbti.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    public User toEntity(RegistrationRequestDto registrationRequestDto) {
        final var user = new User();

        user.setEmail(registrationRequestDto.email());
        user.setNickname(registrationRequestDto.nickname());
        user.setPassword(registrationRequestDto.password());
        user.setLatitude(registrationRequestDto.latitude());
        user.setLongitude(registrationRequestDto.longitude());
        user.setMbtiType(registrationRequestDto.mbti());
        user.setName(registrationRequestDto.name());
        user.setSurname(registrationRequestDto.surname());
        user.setAge(registrationRequestDto.age());
        user.setGender(registrationRequestDto.gender());

        return user;
    }

    public RegistrationResponseDto toRegistrationResponseDto(
            final User user) {

        return new RegistrationResponseDto(
                user.getEmail(), user.getNickname());
    }

}