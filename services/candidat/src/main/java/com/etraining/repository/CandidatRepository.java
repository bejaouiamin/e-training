package com.etraining.repository;

import com.etraining.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatRepository extends JpaRepository<Candidat , Long> {
    Optional<Candidat> findByKeycloakId(String keycloakId);
    Optional<Candidat> findByEmail(String email);
    List<Candidat> id(Long id);
}
