package com.training.repository;

import com.training.entities.Resource;
import com.training.entities.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByLessonIdAndType(Long lessonId, ResourceType type);
    List<Resource> findByLessonId(Long lessonId);
    List<Resource> findByAuthorKeycloakId(String authorKeycloakId);
    List<Resource> findByAuthorKeycloakIdAndType(String authorKeycloakId, ResourceType type);


}
