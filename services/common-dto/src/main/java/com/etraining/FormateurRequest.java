package com.etraining;


import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Map;

public record FormateurRequest(
        String id,
        String keycloakId,
        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire")
        @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
        String prenom,

        @Email(message = "Format d'email invalide")
        @NotBlank(message = "L'email est obligatoire")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        @Pattern(
                regexp = "^(\\+33|0)[1-9]([0-9]{8})$",
                message = "Format de téléphone invalide"
        )
        String telephone,

        List<String> specialites,

        List<String> certifications,

        @Min(value = 0, message = "L'expérience ne peut pas être négative")
        Integer experienceAnnees,


        StatutFormateur statut,

        Map<Integer, DisponibiliteJour> disponibilites
) {
}
