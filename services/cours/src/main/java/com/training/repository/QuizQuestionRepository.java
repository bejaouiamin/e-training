package com.training.repository;

import com.training.entities.Lesson;
import com.training.entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByResourceId(Long resourceId);
    List<QuizQuestion> findByResource_Lesson_Id(Long lessonId);
}
