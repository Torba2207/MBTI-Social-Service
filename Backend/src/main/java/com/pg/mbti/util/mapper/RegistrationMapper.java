package com.pg.mbti.util.mapper;

import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.dto.auth.RegistrationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {
    public RegistrationResponseDto toRegistrationResponseDto(RegistrationRequestDto registrationRequestDto) {
        return new RegistrationResponseDto(
                registrationRequestDto.email(),
                registrationRequestDto.nickname()
        );
    }
}