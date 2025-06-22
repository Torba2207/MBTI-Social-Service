package com.pg.mbti.repository;

import com.pg.mbti.model.questions.AnswerSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link AnswerSet} entities.
 * Provides standard CRUD operations and custom queries for sets of answers.
 */
@Repository
public interface AnswerSetRepository extends JpaRepository<AnswerSet, UUID> {
}