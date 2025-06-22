package com.pg.mbti.service;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.List;

/**
 * Service class to provide access to enumerated types used in the application.
 * This can be useful for populating dropdowns or validating inputs on the client side.
 */
@Service
@Slf4j // Enable logging for this class
public class EnumsService {

    /**
     * Retrieves a list of all available user roles.
     *
     * @return A {@link List} of {@link Role} enums.
     */
    public List<Role> getAllUserRoles() {
        log.debug("Fetching all user roles."); // Log fetching all user roles
        return List.of(Role.values());
    }

    /**
     * Retrieves a list of all available gender options.
     *
     * @return A {@link List} of {@link Gender} enums.
     */
    public List<Gender> getAllUserGenders() {
        log.debug("Fetching all user genders."); // Log fetching all user genders
        return List.of(Gender.values());
    }

    /**
     * Retrieves a list of all available MBTI types.
     *
     * @return A {@link List} of {@link MBTIType} enums.
     */
    public List<MBTIType> getAllMBTITypes() {
        log.debug("Fetching all MBTI types."); // Log fetching all MBTI types
        return List.of(MBTIType.values());
    }

    /**
     * Retrieves a list of all available pronoun options.
     *
     * @return A {@link List} of {@link Pronouns} enums.
     */
    public List<Pronouns> getAllPronouns() {
        log.debug("Fetching all pronoun options."); // Log fetching all pronoun options
        return List.of(Pronouns.values());
    }
}