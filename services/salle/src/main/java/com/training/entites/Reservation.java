package com.training.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    private Long sessionFormationId;
    private String formateurKeycloakId;
    private String reserveParKeycloakId; // Admin qui a réservé

    private Instant dateDebut;
    private Instant dateFin;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private String motif;
    private Instant createdAt;
}