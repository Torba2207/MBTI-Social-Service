package com.pg.mbti.controllers;

import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.services.EnumsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/enums")
@Tag(name = "Enums Management", description = "Endpoints for getting enums present in service")
public class EnumController {

    EnumsService enumsService;

    @GetMapping("/roles")
    @Operation(summary = "Get user roles", description = "Fetch all roles available for user profile")
    public ResponseEntity<List<Role>> getAllUserRoles() {
        return ResponseEntity.ok(enumsService.getAllUserRoles());
    }

    @GetMapping("/genders")
    @Operation(summary = "Get user genders", description = "Fetch all genders available for user profile")
    public ResponseEntity<List<Gender>> getAllUserGenders() {
        return ResponseEntity.ok(enumsService.getAllUserGenders());
    }

    @GetMapping("/mbti")
    @Operation(summary = "Get MBTI types", description = "Fetch all MBTI personality types available in the system")
    public ResponseEntity<List<MBTIType>> getAllMBTITypes() {
        return ResponseEntity.ok(enumsService.getAllMBTITypes());
    }

    @GetMapping("/pronouns")
    @Operation(summary = "Get pronouns", description = "Fetch all pronouns available for user profile selection")
    public ResponseEntity<List<Pronouns>> getAllPronouns() {
        return ResponseEntity.ok(enumsService.getAllPronouns());
    }
}