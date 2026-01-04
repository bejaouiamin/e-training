package com.training.repository;

import com.training.entites.Reservation;
import com.training.entites.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findBySalleId(Long salleId);

    List<Reservation> findByFormateurKeycloakId(String formateurKeycloakId);

    @Query("SELECT r FROM Reservation r WHERE r.salle.id = :salleId " +
            "AND r.status != 'ANNULEE' " +
            "AND ((r.dateDebut <= :fin AND r.dateFin >= :debut))")
    List<Reservation> findConflictingReservations(
            @Param("salleId") Long salleId,
            @Param("debut") Instant debut,
            @Param("fin") Instant fin);

    List<Reservation> findBySalleIdAndStatus(Long salleId, ReservationStatus status);
}
