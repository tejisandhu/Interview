package com.interview.quiz.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.quiz.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String username);
}