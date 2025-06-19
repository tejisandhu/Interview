package com.interview.quiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.quiz.Entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

}
