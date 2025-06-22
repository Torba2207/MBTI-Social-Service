package com.pg.mbti.repository;

import com.pg.mbti.model.questions.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Answer} entities.
 * Provides standard CRUD operations and custom queries for answers.
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}