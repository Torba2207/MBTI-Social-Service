package com.pg.mbti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "friendships")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User senderId;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id",
            referencedColumnName = "id",
            nullable = false
    ) private User receiverId;

    @Column(name = "is_pending")
    private boolean isPending;

    @Column(name = "start_date")
    private Date startDate;
}
