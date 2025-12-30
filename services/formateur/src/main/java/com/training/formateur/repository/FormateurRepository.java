package com.training.formateur.repository;

import com.training.formateur.entites.Formateur;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FormateurRepository extends MongoRepository<Formateur, String> {
    Optional<Formateur> findByKeycloakId(String keycloakId);
}

