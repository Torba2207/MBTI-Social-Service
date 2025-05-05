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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final TagsRepository tagsRepository;
    private final MbtiCompatibilityCalculator compatibilityCalculator;
    private final PhotoService photoService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    public User getUserByNickname(String username) {
        return usersRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void updateUserProfile(String name, UserUpdateDto dto) {
        User user = getUserByNickname(name);

        Optional.ofNullable(dto.latitude()).ifPresent(user::setLatitude);
        Optional.ofNullable(dto.longitude()).ifPresent(user::setLongitude);
        Optional.ofNullable(dto.gender()).ifPresent(user::setGender);
        Optional.ofNullable(dto.description()).ifPresent(user::setDescription);
        Optional.ofNullable(dto.links()).ifPresent(user::setLinks);
        Optional.ofNullable(dto.pronouns()).ifPresent(user::setPronouns);
        Optional.ofNullable(dto.tagIds()).ifPresent(tagIds -> user.setTags(tagsRepository.findAllByIdIn(tagIds)));

        Optional.ofNullable(dto.birthday()).ifPresent(birthday -> {
            try {
                user.setBirthday(DATE_FORMAT.parse(birthday));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
            }
        });

        usersRepository.save(user);
    }

    public List<User> searchUsers(UserSearchDto search) {
        MBTIType referenceType = search.referenceType();
        boolean sortByCompatibility = referenceType != null && "compatibility".equals(search.sortBy());
        boolean descending = "desc".equalsIgnoreCase(search.sortDirection());

        Sort sort = Sort.unsorted();
        if (!sortByCompatibility && StringUtils.isNotEmpty(search.sortBy())) {
            sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC, search.sortBy());
        }

        List<User> users = usersRepository.findUsersByFilters(
                search.name(), search.surname(), search.mbtiType(),
                search.gender(), search.tagIds(), sort);

        if (sortByCompatibility) {
            Comparator<User> comparator = Comparator.comparing(
                    user -> compatibilityCalculator.calculateCompatibility(user.getMbtiType(), referenceType));
            users.sort(descending ? comparator.reversed() : comparator);
        }

        return users;
    }

    public String uploadProfilePhoto(String username, MultipartFile file) {
        String fileName = photoService.uploadPhoto(file);
        User user = getUserByNickname(username);
        user.setProfilePicture(fileName);
        usersRepository.save(user);
        return fileName;
    }

    public UserProfileDto getUserProfileByNickname(String name) {
        return UserMapper.toUserProfileDto(getUserByNickname(name));
    }

    public Resource getProfilePhoto(String name) {
        User user = getUserByNickname(name);
        String fileName = user.getProfilePicture();

        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("No profile photo found for user");
        }

        return photoService.getPhoto(fileName);
    }
}