// File: services/cours/src/main/java/com/training/repository/LessonRepository.java
package com.training.repository;

import com.training.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByThemeIdAndSequenceOrder(Long themeId, Integer sequenceOrder);
}
