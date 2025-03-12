package com.pg.mbti.entity.questions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "answer_set")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnswerSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Question question;
}
