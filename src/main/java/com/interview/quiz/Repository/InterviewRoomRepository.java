package com.interview.quiz.Repository;

import com.interview.quiz.Entity.InterviewRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, String> {
}
