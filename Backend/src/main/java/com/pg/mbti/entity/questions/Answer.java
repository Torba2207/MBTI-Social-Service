package com.pg.mbti.entity.questions;

import com.pg.mbti.entity.MBTIType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "answer_set_id",
            referencedColumnName = "id",
            nullable = false
    )
    private AnswerSet answerSet;

    @Column(name = "mbti")
    @Enumerated(EnumType.STRING)
    private MBTIType mbtiType;
}
