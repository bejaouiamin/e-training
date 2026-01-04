package com.training.repository;

import com.training.entites.Salle;
import com.training.entites.SalleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    List<Salle> findByStatus(SalleStatus status);
    List<Salle> findByCapaciteGreaterThanEqual(Integer capacite);
}
