package com.pg.mbti.entity.user;

import com.pg.mbti.entity.MBTIType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@Setter
public class User {
    @Setter(AccessLevel.NONE)
    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "nickname")
    private String nickname;

    @Getter(AccessLevel.NONE)
    @Column(name = "password_hash")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "mbti")
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private MBTIType mbtiType;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
