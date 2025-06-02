package com.pg.mbti.util.mapper;

import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.model.User;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

/**
 * A utility class for mapping {@link User} entities to {@link UserProfileDto} data transfer objects.
 * This class is marked as a Spring Component to be managed by the Spring IoC container.
 */
@Component
@Slf4j // Enable logging for this class
public class UserMapper {
    /**
     * Converts a {@link User} entity to a {@link UserProfileDto}.
     * This method extracts relevant user profile information from the entity and populates a DTO.
     *
     * @param user The {@link User} entity to convert.
     * @return A new {@link UserProfileDto} containing the user's profile details.
     */
    public static UserProfileDto toUserProfileDto(final User user) {
        log.debug("Mapping User entity with nickname {} to UserProfileDto.", user.getNickname()); // Log the mapping process
        return new UserProfileDto(
                user.getEmail(), // Map the user's email
                user.getNickname(), // Map the user's nickname
                user.getName(), // Map the user's name
                user.getSurname(), // Map the user's surname
                user.getBirthday(), // Map the user's birthday
                user.getMbtiType(), // Map the user's MBTI type
                user.getLatitude(), // Map the user's latitude
                user.getLongitude(), // Map the user's longitude
                user.getGender(), // Map the user's gender
                user.getProfilePicture(), // Map the user's profile picture URL
                user.getDescription(), // Map the user's description
                user.getPronouns(), // Map the user's pronouns
                user.getLinks(), // Map the user's links
                user.getTags()); // Map the user's tags
    }
}