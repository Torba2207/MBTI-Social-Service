package com.pg.mbti.entity.questions;

import com.pg.mbti.enums.MBTIType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "mbti")
    @Enumerated(EnumType.STRING)
    private MBTIType mbtiType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "answers", nullable = false)
    private List<AnswerSet> answers;
}
