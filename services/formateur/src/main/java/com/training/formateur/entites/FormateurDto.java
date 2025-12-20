package com.training.formateur.entites;

import com.training.formateur.Request.FormateurRequest;
import com.training.formateur.Response.FormateurResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class FormateurDto {
    private final PasswordEncoder passwordEncoder;

    public FormateurDto(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Convertit une FormateurRequest en entité Formateur
     */
    public Formateur toEntity(FormateurRequest request) {
        if (request == null) {
            return null;
        }

        return Formateur.builder()
                .nom(request.nom())
                .prenom(request.prenom())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .telephone(request.telephone())
                .specialites(request.specialites())
                .certifications(request.certifications())
                .experienceAnnees(request.experienceAnnees())
                .statut(request.statut())
                .disponibilites(request.disponibilites())
                .historique(new ArrayList<>())
                .dateCreation(LocalDateTime.now())
                .dateModification(LocalDateTime.now())
                .build();
    }

    /**
     * Convertit une entité Formateur en FormateurResponse
     */
    public FormateurResponse toResponse(Formateur formateur) {
        if (formateur == null) {
            return null;
        }

        return new FormateurResponse(
                formateur.getId(),
                formateur.getNom(),
                formateur.getPrenom(),
                formateur.getEmail(),
                formateur.getPassword(),
                formateur.getTelephone(),
                formateur.getSpecialites(),
                formateur.getCertifications(),
                formateur.getExperienceAnnees(),
                formateur.getStatut(),
                formateur.getDisponibilites(),
                formateur.getDateCreation(),
                formateur.getDateModification(),
                formateur.getHistorique()
        );
    }

    /**
     * Convertit une liste de Formateur en liste de FormateurResponse
     */
    public List<FormateurResponse> toResponseList(List<Formateur> formateurs) {
        if (formateurs == null) {
            return null;
        }

        return formateurs.stream()
                .map(this::toResponse)
                .toList();
    }
}


