package com.training.repository;

import com.training.entities.CandidateThemeEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CandidateThemeEnrollmentRepository extends JpaRepository<CandidateThemeEnrollment, Long> {

    List<CandidateThemeEnrollment> findByCandidateKeycloakId(String candidateKeycloakId);

    Optional<CandidateThemeEnrollment> findByCandidateKeycloakIdAndThemeId(String candidateKeycloakId, Long themeId);

    boolean existsByCandidateKeycloakIdAndThemeId(String candidateKeycloakId, Long themeId);
}
