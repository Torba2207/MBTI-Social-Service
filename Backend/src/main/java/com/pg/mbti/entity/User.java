package com.pg.mbti.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class User {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "mbti", nullable = false)
    @Enumerated(EnumType.STRING)
    private MBTIType mbtiType;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "birthday", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name = "user_links", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "link")
    private List<String> links;

    @Enumerated(EnumType.STRING)
    @Column(name = "pronouns", nullable = false)
    private Pronouns pronouns;

    @Column(name = "profile_photo")
    private String profilePicture;

    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
}
