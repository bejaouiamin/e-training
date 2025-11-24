package com.training.formateur.service;

import com.training.formateur.Request.FormateurRequest;
import com.training.formateur.Response.FormateurResponse;
import com.training.formateur.entites.ActiviteFormateur;
import com.training.formateur.entites.Formateur;
import com.training.formateur.entites.FormateurDto;
import com.training.formateur.exception.FormateurNotFoundException;
import com.training.formateur.repository.FormateurRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormateurService {
    private final FormateurRepository repository;
    private final FormateurDto formateurDto;

    public String createFormateur(FormateurRequest request) {
        var  formateur = this.repository.save(this.formateurDto.toEntity(request));
        return formateur.getId();
    }

    public void updateFormateur(FormateurRequest request) {
        var  formateur = this.repository.findById(request.id())
                .orElseThrow(() -> new FormateurNotFoundException(
                        String.format("cannot find this formateur with the provider ID:%s", request.id())));
        this.mergeFormateur(formateur, request);
        this.repository.save(formateur);
    }

    private void mergeFormateur(Formateur formateur, FormateurRequest request) {
        if (StringUtils.isNotBlank(request.nom())) {
            formateur.setNom(request.nom());
        }

        if (StringUtils.isNotBlank(request.prenom())) {
            formateur.setPrenom(request.prenom());
        }

        if (StringUtils.isNotBlank(request.email())) {
            formateur.setEmail(request.email());
        }

        if (StringUtils.isNotBlank(request.telephone())) {
            formateur.setTelephone(request.telephone());
        }

        if (request.specialites() != null && !request.specialites().isEmpty()) {
            formateur.setSpecialites(request.specialites());
        }

        if (request.certifications() != null && !request.certifications().isEmpty()) {
            formateur.setCertifications(request.certifications());
        }

        if (request.experienceAnnees() != null) {
            formateur.setExperienceAnnees(request.experienceAnnees());
        }

        if (request.statut() != null) {
            formateur.setStatut(request.statut());
        }

        if (request.disponibilites() != null && !request.disponibilites().isEmpty()) {
            formateur.setDisponibilites(request.disponibilites());
        }

        formateur.setDateModification(LocalDateTime.now());
        if (formateur.getHistorique() == null) {
            formateur.setHistorique(new ArrayList());
        }

        String description = "Mise à jour des informations du formateur";
        String typeActivite = "MODIFICATION";
        String utilisateurAction = "SYSTEM";
        formateur.getHistorique().add(new ActiviteFormateur(
                typeActivite,           // 1er paramètre → typeActivite ✅
                description,            // 2ème paramètre → description ✅
                LocalDateTime.now(),    // 3ème paramètre → dateActivite ✅
                utilisateurAction       // 4ème paramètre → utilisateurAction ✅
        ));
    }

    public FormateurResponse findById(String id) {
        var formateur = this.repository.findById(id)
                .orElseThrow(() -> new FormateurNotFoundException(
                        String.format("Cannot find this formateur with the provider ID: %s", id)
                ));
        return formateurDto.toResponse(formateur);
    }


    public List<FormateurResponse> findAll() {
        var formateurs = this.repository.findAll();
        return formateurDto.toResponseList(formateurs);
    }


    public void deleteById(String id) {
        if (!this.repository.existsById(id)) {
            throw new FormateurNotFoundException(
                    String.format("Cannot find this formateur with the provider ID: %s", id)
            );
        }
        this.repository.deleteById(id);
    }


}

