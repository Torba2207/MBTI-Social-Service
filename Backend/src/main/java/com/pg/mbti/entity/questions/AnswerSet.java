package com.pg.mbti.entity.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "answer_set")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AnswerSet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Question question;

    @Column(name = "is_yes", nullable = false)
    private boolean isYes;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    @JsonIgnore
    private Answer answer;
}
