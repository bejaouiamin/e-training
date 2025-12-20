package com.etraining.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String password;

    // Métadonnées
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();
    private LocalDateTime dateDerniereConnexion = LocalDateTime.now();

    @Embedded
    private Address address;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCandidat statut = StatutCandidat.ACTIF;
}