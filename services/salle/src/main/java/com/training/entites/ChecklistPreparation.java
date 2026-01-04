package com.training.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "checklist_preparation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistPreparation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private String gestionnaireKeycloakId;

    @ElementCollection
    @CollectionTable(name = "checklist_items")
    private List<ChecklistItem> items;

    private boolean preparationComplete;
    private Instant completedAt;
}