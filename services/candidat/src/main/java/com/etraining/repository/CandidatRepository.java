package com.etraining.repository;

import com.etraining.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatRepository extends JpaRepository<Candidat , Long> {

    List<Candidat> id(Long id);
}
