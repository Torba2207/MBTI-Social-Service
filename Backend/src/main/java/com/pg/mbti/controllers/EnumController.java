package com.pg.mbti.controllers;

import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Role;
import com.pg.mbti.services.EnumsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/enums")
public class EnumController {

    EnumsService enumsService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllUserRoles() {
        return ResponseEntity.ok(enumsService.getAllUserRoles());
    }

    @GetMapping("/genders")
    public ResponseEntity<List<Gender>> getAllUserGenders() {
        return ResponseEntity.ok(enumsService.getAllUserGenders());
    }

    @GetMapping("/mbti")
    public ResponseEntity<List<MBTIType>> getAllMBTITypes() {
        return ResponseEntity.ok(enumsService.getAllMBTITypes());
    }
}