package com.pg.mbti.services;

import com.pg.mbti.dto.UserSearchDto;
import com.pg.mbti.dto.UserUpdateDto;
import com.pg.mbti.entity.Tag;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.exceptions.ResourceNotFoundException;
import com.pg.mbti.repositories.TagsRepository;
import com.pg.mbti.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final TagsRepository tagsRepository;

    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    public User getUserByNickname(final String username) {
        return usersRepository.findByNickname(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void updateUser(User user) {
        usersRepository.save(user);
    }

    public void updateUserProfile(String name, UserUpdateDto userUpdateDto) {
        final var user = usersRepository.findByNickname(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userUpdateDto.latitude() != null) {
            user.setLatitude(userUpdateDto.latitude());
        }
        if (userUpdateDto.longitude() != null) {
            user.setLongitude(userUpdateDto.longitude());
        }
        if (userUpdateDto.birthday() != null) {
            try {
                Date birthday = new SimpleDateFormat("yyyy-MM-dd").parse(userUpdateDto.birthday());
                user.setBirthday(birthday);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
            }
        }
        if (userUpdateDto.gender() != null) {
            user.setGender(userUpdateDto.gender());
        }
        if (userUpdateDto.description() != null) {
            user.setDescription(userUpdateDto.description());
        }
        if (userUpdateDto.links() != null) {
            user.setLinks(userUpdateDto.links());
        }
        if (userUpdateDto.pronouns() != null) {
            user.setPronouns(userUpdateDto.pronouns());
        }
        if (userUpdateDto.tagIds() != null) {
            Set<Tag> tags = tagsRepository.findAllByIdIn(userUpdateDto.tagIds());
            user.setTags(tags);
        }
        usersRepository.save(user);
    }

    public List<User> searchUsers(UserSearchDto searchDto) {
        String name = searchDto.name();
        String surname = searchDto.surname();
        MBTIType mbtiType = searchDto.mbtiType();
        Gender gender = searchDto.gender();
        Set<UUID> tagIds = searchDto.tagIds();

        Sort sort = Sort.unsorted();
        if (searchDto.sortBy() != null && !searchDto.sortBy().isEmpty()) {
            Sort.Direction direction = searchDto.sortDirection() != null &&
                    searchDto.sortDirection().equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, searchDto.sortBy());
        }

        return usersRepository.findUsersByFilters(name, surname, mbtiType, gender, tagIds, sort);
    }
}