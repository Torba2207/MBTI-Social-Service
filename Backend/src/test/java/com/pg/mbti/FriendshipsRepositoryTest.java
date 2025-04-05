package com.pg.mbti;

import com.pg.mbti.entity.Friendship;
import com.pg.mbti.entity.User;
import com.pg.mbti.enums.Gender;
import com.pg.mbti.enums.MBTIType;
import com.pg.mbti.enums.Pronouns;
import com.pg.mbti.enums.Role;
import com.pg.mbti.repositories.FriendshipsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendshipsRepositoryTest {
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/MBTI_DATABASE");
        registry.add("spring.datasource.username", () -> "mbtiadmin");
        registry.add("spring.datasource.password", () -> "admin");
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FriendshipsRepository friendshipsRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .nickname("user1")
                .name("user1")
                .surname("user1")
                .password("password")
                .email("user1@mbti.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();

        user2 = User.builder()
                .nickname("user2")
                .name("user2")
                .surname("user2")
                .password("password")
                .email("user2@mbti.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();

        user3 = User.builder()
                .nickname("user3")
                .name("user3")
                .surname("user3")
                .password("password")
                .email("user3@mbti.com")
                .mbtiType(MBTIType.INTJ)
                .birthday(Date.valueOf("2000-01-01"))
                .gender(Gender.MALE)
                .pronouns(Pronouns.HE_HIM)
                .role(Role.VERIFIED)
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
    }

    @Test
    void getMyPendingFriendships_WhenUserHasPendingRequests_ReturnsRequests() {
        // Create pending friendship
        Friendship pendingFriendship = new Friendship(user2, user1);
        pendingFriendship.setPending(true);
        entityManager.persist(pendingFriendship);
        entityManager.flush();

        List<Friendship> pendingFriendships = friendshipsRepository.getMyPendingFriendships("user1");

        assertThat(pendingFriendships).hasSize(1);
        assertThat(pendingFriendships.getFirst().getSenderId().getNickname()).isEqualTo("user2");
        assertThat(pendingFriendships.getFirst().getReceiverId().getNickname()).isEqualTo("user1");
    }

    @Test
    void getMyPendingFriendships_WhenUserHasNoPendingRequests_ReturnsEmptyList() {
        List<Friendship> pendingFriendships = friendshipsRepository.getMyPendingFriendships("user1");
        assertThat(pendingFriendships).isEmpty();
    }

    @Test
    void getFriendshipsByNickname_WhenUserHasAcceptedFriendships_ReturnsFriendships() {
        // Create accepted friendship where user is receiver
        Friendship friendship1 = new Friendship(user2, user1);
        friendship1.setPending(false);

        // Create accepted friendship where user is sender
        Friendship friendship2 = new Friendship(user1, user3);
        friendship2.setPending(false);

        entityManager.persist(friendship1);
        entityManager.persist(friendship2);
        entityManager.flush();

        List<Friendship> friendships = friendshipsRepository.getFriendshipsByNickname("user1");

        assertThat(friendships).hasSize(2);
    }

    @Test
    void getFriendshipsByNickname_WhenUserHasNoAcceptedFriendships_ReturnsEmptyList() {
        List<Friendship> friendships = friendshipsRepository.getFriendshipsByNickname("user1");
        assertThat(friendships).isEmpty();
    }

    @Test
    void getFriendshipsByNickname_DoesNotIncludePendingFriendships() {
        // Create pending friendship
        Friendship pendingFriendship = new Friendship(user2, user1);
        pendingFriendship.setPending(true);
        entityManager.persist(pendingFriendship);
        entityManager.flush();

        List<Friendship> friendships = friendshipsRepository.getFriendshipsByNickname("user1");
        assertThat(friendships).isEmpty();
    }

    @Test
    void findByFriends_WhenFriendshipExists_ReturnsFriendship() {
        Friendship friendship = new Friendship(user1, user2);
        entityManager.persist(friendship);
        entityManager.flush();

        Friendship found = friendshipsRepository.findByFriends("user1", "user2");

        assertThat(found).isNotNull();
        assertThat(found.getSenderId().getNickname()).isEqualTo("user1");
        assertThat(found.getReceiverId().getNickname()).isEqualTo("user2");
    }

    @Test
    void findByFriends_WorksRegardlessOfParameterOrder() {
        Friendship friendship = new Friendship(user1, user2);
        entityManager.persist(friendship);
        entityManager.flush();

        Friendship foundForward = friendshipsRepository.findByFriends("user1", "user2");
        Friendship foundReverse = friendshipsRepository.findByFriends("user2", "user1");

        assertThat(foundForward).isEqualTo(foundReverse);
    }

    @Test
    void findByFriends_WhenFriendshipDoesNotExist_ReturnsNull() {
        Friendship found = friendshipsRepository.findByFriends("user1", "user2");
        assertThat(found).isNull();
    }

    @Test
    void existsByFriends_WhenFriendshipExists_ReturnsTrue() {
        Friendship friendship = new Friendship(user1, user2);
        entityManager.persist(friendship);
        entityManager.flush();

        boolean exists = friendshipsRepository.existsByFriends("user1", "user2");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByFriends_WhenFriendshipDoesNotExist_ReturnsFalse() {
        boolean exists = friendshipsRepository.existsByFriends("user1", "user2");
        assertThat(exists).isFalse();
    }

    @Test
    void existsByFriends_WorksRegardlessOfParameterOrder() {
        Friendship friendship = new Friendship(user1, user2);
        entityManager.persist(friendship);
        entityManager.flush();

        boolean existsForward = friendshipsRepository.existsByFriends("user1", "user2");
        boolean existsReverse = friendshipsRepository.existsByFriends("user2", "user1");

        assertThat(existsForward).isTrue();
        assertThat(existsReverse).isTrue();
    }
}