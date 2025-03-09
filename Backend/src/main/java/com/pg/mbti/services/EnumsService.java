package com.pg.mbti.services;

import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumsService {

    public List<Role> getAllUserRoles() {
        return List.of(Role.values());
    }

    public List<Gender> getAllUserGenders() {
        return List.of(Gender.values());
    }

    public List<MBTIType> getAllMBTITypes() {
        return List.of(MBTIType.values());
    }
}
