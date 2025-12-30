package com.training.formateur.service;


import com.etraining.*;
import com.training.formateur.Request.FormateurRequest;
import com.training.formateur.Request.LessonRequest;
import com.training.formateur.Request.QuizRequest;
import com.training.formateur.Response.FormateurResponse;
import com.training.formateur.entites.ActiviteFormateur;
import com.training.formateur.entites.Formateur;
import com.training.formateur.entites.FormateurDto;
import com.training.formateur.exception.FormateurNotFoundException;
import com.training.formateur.repository.FormateurRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormateurService {
    private final FormateurRepository repository;
    private final FormateurDto formateurDto;
    private final EventPublisher eventPublisher;
    private final FileStorageService fileStorageService;

    public String createFormateur(FormateurRequest request) {
        var  formateur = this.repository.save(this.formateurDto.toEntity(request));
        return formateur.getId();
    }

    public void createTheme(String keycloakId, String title, String description, Long categoryId, Integer dureeHeures) {
        Formateur formateur = repository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));

        ThemeCreatedEvent event = new ThemeCreatedEvent(
                UUID.randomUUID().toString(),
                keycloakId,
                title,
                description,
                categoryId,
                dureeHeures  // ✅ Utiliser le paramètre Integer au lieu de getNano()
        );
        eventPublisher.publishThemeCreated(event);
    }
    public void createLesson(String keycloakId, LessonRequest request) {
        Formateur formateur = repository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));

        List<com.etraining.ResourceDto> resourceDtos = new ArrayList<>();

        if (request.getResourceTitles() != null) {
            for (int i = 0; i < request.getResourceTitles().size(); i++) {
                String url = null;

                String typeStr = (request.getResourceTypes() != null && i < request.getResourceTypes().size())
                        ? request.getResourceTypes().get(i)
                        : null;

                if (request.getFiles() != null && i < request.getFiles().size()) {
                    MultipartFile file = request.getFiles().get(i);
                    if (!file.isEmpty()) {
                        // on passe la string au validateur si il attend une string
                        fileStorageService.validateFileType(file, typeStr);
                        url = fileStorageService.storeFile(file);
                    }
                }

                com.etraining.ResourceType parsedType = null;
                if (typeStr != null) {
                    try {
                        parsedType = com.etraining.ResourceType.valueOf(typeStr.trim().toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException e) {
                        log.warn("Type de ressource inconnu fourni par le formateur: {}", typeStr);
                        // parsedType reste null ou définir une valeur par défaut ici, ex: ResourceType.PDF
                        // parsedType = com.etraining.ResourceType.PDF;
                    }
                }

                resourceDtos.add(new com.etraining.ResourceDto(
                        request.getResourceTitles().get(i),
                        url,
                        parsedType,
                        null
                ));
            }
        }

        LessonCreatedEvent event = new LessonCreatedEvent(
                UUID.randomUUID().toString(),
                keycloakId,
                request.getThemeId(),
                request.getTitle(),
                request.getDescription(),
                request.getSequenceOrder(),
                resourceDtos
        );

        eventPublisher.publishLessonCreated(event);
    }


    public void createQuiz(String keycloakId, QuizRequest request) {
        Formateur formateur = repository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));

        List<QuizQuestionDto> questionDtos = request.getQuestions().stream()
                .map(q -> new QuizQuestionDto(
                        q.getQuestionText(),
                        q.getAnswers().stream()
                                .map(a -> new QuizAnswerDto(a.getAnswerText(), a.isCorrect()))
                                .toList()
                ))
                .toList();

        QuizCreatedEvent event = new QuizCreatedEvent(
                UUID.randomUUID().toString(),
                keycloakId,
                request.getTitle(),
                request.getLessonId(),
                request.getPassingScore(),
                questionDtos
        );
        eventPublisher.publishQuizCreated(event);
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

