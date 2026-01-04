package com.training.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "salles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private Integer capacite;
    private String localisation;

    @ElementCollection
    @CollectionTable(name = "salle_equipements")
    private List<String> equipements;

    @ElementCollection
    @CollectionTable(name = "salle_photos")
    private List<String> photosUrls;

    @Enumerated(EnumType.STRING)
    private SalleStatus status;

    private Instant createdAt;
    private Instant updatedAt;
}
