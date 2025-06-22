package com.pg.mbti.util.mapper;

import com.pg.mbti.dto.auth.RegistrationRequestDto;
import com.pg.mbti.dto.auth.RegistrationResponseDto;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

/**
 * A utility class for mapping registration-related DTOs.
 * This class is marked as a Spring Component to be managed by the Spring IoC container.
 */
@Component
@Slf4j // Enable logging for this class
public class RegistrationMapper {
    /**
     * Converts a {@link RegistrationRequestDto} to a {@link RegistrationResponseDto}.
     * This typically involves selecting a subset of data from the request to return as a response.
     *
     * @param registrationRequestDto The {@link RegistrationRequestDto} to convert.
     * @return A new {@link RegistrationResponseDto} containing the email and nickname.
     */
    public RegistrationResponseDto toRegistrationResponseDto(RegistrationRequestDto registrationRequestDto) {
        log.debug("Mapping RegistrationRequestDto for email {} to RegistrationResponseDto.", registrationRequestDto.email()); // Log the mapping process
        return new RegistrationResponseDto(
                registrationRequestDto.email(), // Map the email from the request
                registrationRequestDto.nickname() // Map the nickname from the request
        );
    }
}