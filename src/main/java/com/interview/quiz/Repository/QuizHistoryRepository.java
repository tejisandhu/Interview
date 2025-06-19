package com.interview.quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.quiz.Entity.QuizHistory;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {

}
