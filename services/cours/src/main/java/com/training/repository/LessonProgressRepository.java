package com.training.repository;

import com.training.entities.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    Optional<LessonProgress> findByCandidateKeycloakIdAndResource_Id(String candidateKeycloakId, Long resourceId);
    List<LessonProgress> findByCandidateKeycloakIdAndResource_IdIn(String candidateKeycloakId, List<Long> resourceIds);
//    Optional<LessonProgress> findByCandidateKeycloakIdAndLessonId(String KeycloakId, Long lessonId);
    List<LessonProgress> findByCandidateKeycloakId(String candidateKeycloakId);
    List<LessonProgress> findByCandidateKeycloakIdAndLesson_Id(String candidateKeycloakId, Long lessonId);


}
