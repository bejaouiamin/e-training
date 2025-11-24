package com.training.formateur.repository;

import com.training.formateur.entites.Formateur;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormateurRepository extends MongoRepository<Formateur, String> {
}

