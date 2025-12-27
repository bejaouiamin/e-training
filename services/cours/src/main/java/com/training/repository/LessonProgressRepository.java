package com.training.repository;

import com.training.entities.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByUserIdAndResourceId(Long userId, Long resourceId);
    List<LessonProgress> findByUserIdAndResourceIdIn(Long userId, java.util.List<Long> resourceIds);
}
