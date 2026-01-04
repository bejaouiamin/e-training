package com.training.controller;


import com.training.entites.Reservation;
import com.training.entites.Salle;
import com.training.entites.SalleStatus;
import com.training.service.SalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
@RequiredArgsConstructor
public class SalleController {

    private final SalleService salleService;



    @PostMapping("/add")
    public ResponseEntity<Salle> createSalle(@RequestBody Salle salle) {
        return ResponseEntity.ok(salleService.createSalle(salle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Salle> updateSalle(@PathVariable Long id, @RequestBody Salle salle) {
        return ResponseEntity.ok(salleService.updateSalle(id, salle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Salle>> getAllSalles() {
        return ResponseEntity.ok(salleService.getAllSalles());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Salle>> getSallesDisponibles() {
        return ResponseEntity.ok(salleService.getSallesDisponibles());
    }

    // Réserver une salle pour un formateur
    @PostMapping("/{salleId}/reserver/formateur/{formateurKeycloakId}")
    public ResponseEntity<Reservation> reserverSallePourFormateur(
            @PathVariable Long salleId,
            @PathVariable String formateurKeycloakId,
            @RequestBody Reservation reservation,
            @RequestHeader("X-Keycloak-Id") String adminKeycloakId) {
        return ResponseEntity.ok(
                salleService.reserverSallePourFormateur(salleId, reservation,
                        adminKeycloakId, formateurKeycloakId));
    }

    // Annuler une réservation
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> annulerReservation(
            @PathVariable Long reservationId,
            @RequestParam(required = false) String reason) {
        salleService.annulerReservation(reservationId, reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{salleId}/reservations")
    public ResponseEntity<List<Reservation>> getReservations(@PathVariable Long salleId) {
        return ResponseEntity.ok(salleService.getReservationsBySalle(salleId));
    }

    @GetMapping("/formateur/reservations")
    public ResponseEntity<List<Reservation>> getFormateurReservations(
            @RequestHeader("X-Keycloak-Id") String formateurKeycloakId) {
        return ResponseEntity.ok(salleService.getReservationsForFormateur(formateurKeycloakId));
    }

    @PatchMapping("/{salleId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long salleId,
            @RequestParam SalleStatus status) {
        salleService.updateSalleStatus(salleId, status);
        return ResponseEntity.ok().build();
    }
}
