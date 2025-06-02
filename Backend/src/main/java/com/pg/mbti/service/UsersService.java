package com.pg.mbti.service;

import com.pg.mbti.util.MbtiCompatibilityCalculator;
import com.pg.mbti.dto.UserProfileDto;
import com.pg.mbti.dto.UserSearchDto;
import com.pg.mbti.dto.UserUpdateDto;
import com.pg.mbti.model.User;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.util.mapper.UserMapper;
import com.pg.mbti.repository.TagsRepository;
import com.pg.mbti.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service class for managing user-related operations, including profile management,
 * search, and profile picture uploads.
 */
@Service
@AllArgsConstructor
@Slf4j // Enable logging for this class
public class UsersService {
    private final UsersRepository usersRepository;
    private final TagsRepository tagsRepository;
    private final MbtiCompatibilityCalculator compatibilityCalculator;
    private final PhotoService photoService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Retrieves all registered users.
     *
     * @return A list of all {@link User} entities.
     */
    public List<User> getUsers() {
        log.debug("Fetching all users."); // Log fetching all users
        return usersRepository.findAll();
    }

    /**
     * Retrieves a user by their nickname.
     *
     * @param username The nickname of the user.
     * @return The {@link User} entity if found.
     * @throws UsernameNotFoundException If no user is found with the given nickname.
     */
    public User getUserByNickname(String username) {
        log.debug("Attempting to retrieve user by nickname: {}", username); // Log user retrieval attempt
        return usersRepository.findByNickname(username)
                .orElseThrow(() -> {
                    log.warn("User not found with nickname: {}", username); // Log user not found
                    return new UsernameNotFoundException("User not found");
                });
    }

    /**
     * Updates a user's profile information based on the provided DTO.
     *
     * @param name The nickname of the user whose profile is to be updated.
     * @param dto The {@link UserUpdateDto} containing the updated profile data.
     * @throws IllegalArgumentException If the birthday format is invalid.
     * @throws UsernameNotFoundException If the user with the given nickname is not found.
     */
    public void updateUserProfile(String name, UserUpdateDto dto) {
        log.info("Attempting to update profile for user: {}", name); // Log profile update attempt
        User user = getUserByNickname(name);

        Optional.ofNullable(dto.latitude()).ifPresent(user::setLatitude);
        Optional.ofNullable(dto.longitude()).ifPresent(user::setLongitude);
        Optional.ofNullable(dto.gender()).ifPresent(user::setGender);
        Optional.ofNullable(dto.description()).ifPresent(user::setDescription);
        Optional.ofNullable(dto.links()).ifPresent(user::setLinks);
        Optional.ofNullable(dto.pronouns()).ifPresent(user::setPronouns);
        Optional.ofNullable(dto.tagIds()).ifPresent(tagIds -> {
            user.setTags(tagsRepository.findAllByIdIn(tagIds));
            log.debug("Updating tags for user {}: {}", name, tagIds); // Log tag update
        });

        Optional.ofNullable(dto.birthday()).ifPresent(birthday -> {
            try {
                user.setBirthday(DATE_FORMAT.parse(birthday));
                log.debug("Updating birthday for user {}: {}", name, birthday); // Log birthday update
            } catch (ParseException e) {
                log.error("Invalid birthday format for user {}: {}", name, birthday, e); // Log invalid date format
                throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
            }
        });

        usersRepository.save(user);
        log.info("User profile updated successfully for user: {}", name); // Log successful profile update
    }

    /**
     * Searches for users based on various filters and sorts the results.
     * Can also sort by MBTI compatibility if a reference type is provided.
     *
     * @param search The {@link UserSearchDto} containing search criteria.
     * @param authenticatedUsername The nickname of the currently authenticated user to exclude from results.
     * @return A list of {@link User} entities matching the search criteria.
     */
    public List<User> searchUsers(UserSearchDto search, String authenticatedUsername) {
        log.info("Searching users with criteria: {}", search); // Log user search criteria
        MBTIType referenceType = search.referenceType();
        boolean sortByCompatibility = referenceType != null && "compatibility".equals(search.sortBy());
        boolean descending = "desc".equalsIgnoreCase(search.sortDirection());

        Sort sort = Sort.unsorted();
        if (!sortByCompatibility && StringUtils.isNotEmpty(search.sortBy())) {
            sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC, search.sortBy());
            log.debug("Applying standard sort by: {} direction: {}", search.sortBy(), search.sortDirection()); // Log standard sort
        }

        List<User> users = usersRepository.findUsersByFilters(
                search.name(), search.surname(), search.mbtiType(),
                search.gender(), search.tagIds(), authenticatedUsername, sort);
        log.debug("Found {} users matching initial filters.", users.size()); // Log initial filtered users

        if (sortByCompatibility) {
            log.debug("Sorting users by MBTI compatibility with reference type: {}", referenceType); // Log compatibility sort
            Comparator<User> comparator = Comparator.comparing(
                    user -> compatibilityCalculator.calculateCompatibility(user.getMbtiType(), referenceType));
            users.sort(descending ? comparator.reversed() : comparator);
            log.debug("Users sorted by compatibility. First user's compatibility: {}",
                    users.isEmpty() ? "N/A" : compatibilityCalculator.calculateCompatibility(users.getFirst().getMbtiType(), referenceType)); // Log first user's compatibility
        }

        return users;
    }

    /**
     * Uploads a profile photo for a specific user.
     *
     * @param username The nickname of the user.
     * @param file The {@link MultipartFile} representing the profile photo.
     * @return The file name of the uploaded photo.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public String uploadProfilePhoto(String username, MultipartFile file) {
        log.info("Attempting to upload profile photo for user: {}", username); // Log profile photo upload attempt
        String fileName = photoService.uploadPhoto(file);
        User user = getUserByNickname(username);
        user.setProfilePicture(fileName);
        usersRepository.save(user);
        log.info("Profile photo updated for user {} to file: {}", username, fileName); // Log successful photo update
        return fileName;
    }

    /**
     * Retrieves the profile DTO for a given user nickname.
     *
     * @param name The nickname of the user.
     * @return The {@link UserProfileDto} of the user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public UserProfileDto getUserProfileByNickname(String name) {
        log.debug("Fetching user profile DTO for nickname: {}", name); // Log profile DTO fetch
        return UserMapper.toUserProfileDto(getUserByNickname(name));
    }

    /**
     * Retrieves the profile photo as a {@link Resource} for a given user.
     *
     * @param name The nickname of the user.
     * @return A {@link Resource} representing the user's profile photo.
     * @throws IllegalArgumentException If no profile photo is found for the user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public Resource getProfilePhoto(String name) {
        log.debug("Fetching profile photo for user: {}", name); // Log profile photo fetch
        User user = getUserByNickname(name);
        String fileName = user.getProfilePicture();

        if (StringUtils.isBlank(fileName)) {
            log.warn("No profile photo found for user: {}", name); // Log no profile photo
            throw new IllegalArgumentException("No profile photo found for user");
        }

        return photoService.getPhoto(fileName);
    }
}