package com.training.service;


import com.etraining.events.ReservationCancelledEvent;
import com.etraining.events.ReservationCreatedEvent;
import com.training.entites.Reservation;
import com.training.entites.ReservationStatus;
import com.training.entites.Salle;
import com.training.entites.SalleStatus;
import com.training.repository.ReservationRepository;
import com.training.repository.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalleService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationEventPublisher eventPublisher;

    // CRUD Salles (inchangé)
    public Salle createSalle(Salle salle) {
        salle.setCreatedAt(Instant.now());
        salle.setStatus(SalleStatus.DISPONIBLE);
        return salleRepository.save(salle);
    }

    public Salle updateSalle(Long id, Salle salleDetails) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
        salle.setNom(salleDetails.getNom());
        salle.setDescription(salleDetails.getDescription());
        salle.setCapacite(salleDetails.getCapacite());
        salle.setEquipements(salleDetails.getEquipements());
        salle.setUpdatedAt(Instant.now());
        return salleRepository.save(salle);
    }

    public void deleteSalle(Long id) {
        salleRepository.deleteById(id);
    }

    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    public List<Salle> getSallesDisponibles() {
        return salleRepository.findByStatus(SalleStatus.DISPONIBLE);
    }

    // Réservation avec publication Kafka
    @Transactional
    public Reservation reserverSallePourFormateur(Long salleId, Reservation reservation,
                                                  String adminKeycloakId, String formateurKeycloakId) {

        List<Reservation> conflits = reservationRepository.findConflictingReservations(
                salleId, reservation.getDateDebut(), reservation.getDateFin());

        if (!conflits.isEmpty()) {
            throw new RuntimeException("Double réservation détectée pour cette période");
        }

        Salle salle = salleRepository.findById(salleId)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));

        if (salle.getStatus() != SalleStatus.DISPONIBLE) {
            throw new RuntimeException("La salle n'est pas disponible");
        }

        reservation.setSalle(salle);
        reservation.setReserveParKeycloakId(adminKeycloakId);
        reservation.setFormateurKeycloakId(formateurKeycloakId);
        reservation.setStatus(ReservationStatus.CONFIRMEE);
        reservation.setCreatedAt(Instant.now());

        salle.setStatus(SalleStatus.RESERVEE);
        salleRepository.save(salle);

        Reservation savedReservation = reservationRepository.save(reservation);

        // Publier l'événement Kafka
        ReservationCreatedEvent event = ReservationCreatedEvent.builder()
                .reservationId(savedReservation.getId())
                .salleId(salle.getId())
                .salleName(salle.getNom())
                .formateurKeycloakId(formateurKeycloakId)
                .sessionFormationId(reservation.getSessionFormationId())
                .startDateTime(reservation.getDateDebut())
                .endDateTime(reservation.getDateFin())
                .createdAt(Instant.now())
                .build();

        eventPublisher.publishReservationCreated(event);

        return savedReservation;
    }

    @Transactional
    public void annulerReservation(Long reservationId, String reason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatus(ReservationStatus.ANNULEE);
        reservationRepository.save(reservation);

        // Remettre la salle disponible
        Salle salle = reservation.getSalle();
        salle.setStatus(SalleStatus.DISPONIBLE);
        salleRepository.save(salle);

        // Publier l'événement d'annulation
        ReservationCancelledEvent event = ReservationCancelledEvent.builder()
                .reservationId(reservationId)
                .salleId(salle.getId())
                .formateurKeycloakId(reservation.getFormateurKeycloakId())
                .reason(reason)
                .cancelledAt(Instant.now())
                .build();

        eventPublisher.publishReservationCancelled(event);
    }

    public List<Reservation> getReservationsBySalle(Long salleId) {
        return reservationRepository.findBySalleId(salleId);
    }

    public List<Reservation> getReservationsForFormateur(String formateurKeycloakId) {
        return reservationRepository.findByFormateurKeycloakId(formateurKeycloakId);
    }

    public void updateSalleStatus(Long salleId, SalleStatus status) {
        Salle salle = salleRepository.findById(salleId)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
        salle.setStatus(status);
        salleRepository.save(salle);
    }
}
