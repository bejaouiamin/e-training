package com.training.formateur.Request;


import com.training.formateur.entites.DisponibiliteJour;
import com.training.formateur.entites.StatutFormateur;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Map;

public record FormateurRequest(
        String id,

        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire")
        @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
        String prenom,

        @Email(message = "Format d'email invalide")
        @NotBlank(message = "L'email est obligatoire")
        String email,

        @Pattern(
                regexp = "^(\\+33|0)[1-9]([0-9]{8})$",
                message = "Format de téléphone invalide"
        )
        String telephone,

        @NotEmpty(message = "Au moins une spécialité est requise")
        List<String> specialites,

        @NotEmpty(message = "Au moins une certification est requise")
        List<String> certifications,

        @Min(value = 0, message = "L'expérience ne peut pas être négative")
        Integer experienceAnnees,

        @NotNull(message = "Le statut est obligatoire")
        StatutFormateur statut,

        Map<Integer, DisponibiliteJour> disponibilites
) {
}

