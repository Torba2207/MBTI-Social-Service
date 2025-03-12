package com.pg.mbti.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@Setter
public class User {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "nickname")
    private String nickname;

    @JsonIgnore
    @Column(name = "password_hash")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "mbti", columnDefinition = "mbti_type")
    @ColumnTransformer(write = "?::mbti_type")
    @Enumerated(EnumType.STRING)
    private MBTIType mbtiType;

    @Column(name = "role", columnDefinition = "user_role")
    @ColumnTransformer(write = "?::user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "user_gender")
    @ColumnTransformer(write = "?::user_gender")
    private Gender gender;

    @Column(name = "profile_photo")
    private String profilePicture;

    public User(String name, String surname, String nickname,
                String password, String email, Double latitude,
                Double longitude, MBTIType mbtiType, Integer age, Gender gender) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mbtiType = mbtiType;
        this.age = age;
        this.gender = gender;
        this.role = Role.ANONYMOUS;
        this.profilePicture = "default.jpg";
    }
}
