package com.training.formateur.Response;

import com.training.formateur.entites.ActiviteFormateur;
import com.training.formateur.entites.DisponibiliteJour;
import com.training.formateur.entites.StatutFormateur;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record FormateurResponse(
        String id,
        String keycloakId,
        String nom,
        String prenom,
        String email,
        String password,
        String telephone,
        List<String> specialites,
        List<String> certifications,
        Integer experienceAnnees,
        StatutFormateur statut,
        Map<Integer, DisponibiliteJour> disponibilites,
        LocalDateTime dateCreation,
        LocalDateTime dateModification,
        List<ActiviteFormateur> historique
) {
}

