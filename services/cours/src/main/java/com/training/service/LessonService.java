package com.training.service;

import com.training.Dtos.LessonProgressDTO;
import com.training.Dtos.LessonWithResourcesDTO;
import com.training.Dtos.ResourceDTO;
import com.training.Dtos.ResourceProgressDTO;
import com.training.entities.*;
import com.training.event.EventPublisher;
import com.training.event.QuizSubmittedEvent;
import com.training.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ResourceRepository resourceRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final EventPublisher eventPublisher;
    private final QuizQuestionRepository questionRepo;

    @Transactional
    public Lesson createLesson(Lesson lesson) {
        // persist lesson + resources (cascade)
        return lessonRepository.save(lesson);
    }
    public List<Lesson> getLessonsByThemeId(Long themeId) {
        return lessonRepository.findByThemeId(themeId);
    }

    public Lesson getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Lesson not found with id: " + lessonId
                ));
    }



    @Transactional
    public void markResourceCompleted(String candidateKeycloakId, Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        Lesson lesson = resource.getLesson(); // la leçon liée à la ressource

        LessonProgress progress = lessonProgressRepository
                .findByCandidateKeycloakIdAndResource_Id(candidateKeycloakId, resourceId)
                .orElse(LessonProgress.builder()
                        .candidateKeycloakId(candidateKeycloakId)
                        .resource(resource)
                        .lesson(lesson)          // \*\*important\*\*
                        .completed(false)
                        .build());

        progress.setCompleted(true);
        progress.setCompletedAt(Instant.now());
        lessonProgressRepository.save(progress);
    }



    @Transactional
    public QuizAttempt submitQuizAttempt(String candidateKeycloakId, Long resourceId, Integer score) {
        Resource res = resourceRepository.findById(resourceId).orElseThrow();

        // Valeur par défaut si passingScore non défini
        Integer passingScore = res.getPassingScore() != null ? res.getPassingScore() : 50;
        boolean passed = score != null && score >= passingScore;

        log.info("Quiz attempt: resourceId={}, score={}, passingScore={}, passed={}",
                resourceId, score, passingScore, passed);

        QuizAttempt attempt = QuizAttempt.builder()
                .candidateKeycloakId(candidateKeycloakId)
                .resource(res)
                .score(score)
                .passed(passed)
                .attemptedAt(Instant.now())
                .build();
        quizAttemptRepository.save(attempt);

        if (passed) {
            markResourceCompleted(candidateKeycloakId, resourceId);
        }

        QuizSubmittedEvent event = new QuizSubmittedEvent(
                candidateKeycloakId,
                resourceId,
                score,
                passed,
                Instant.now(),
                "cours-service"
        );
        eventPublisher.publishQuizSubmitted(event);

        return attempt;
    }

    public boolean canOpenQuiz(String candidateKeycloakId, Long lessonId) {
        List<Resource> videos = resourceRepository.findByLessonIdAndType(lessonId, ResourceType.VIDEO);
        List<Long> videoIds = videos.stream().map(Resource::getId).collect(Collectors.toList());
        if (videoIds.isEmpty()) {
            return true;
        }

        List<LessonProgress> progresses =
                lessonProgressRepository.findByCandidateKeycloakIdAndResource_IdIn(candidateKeycloakId, videoIds);

        long done = progresses.stream().filter(LessonProgress::isCompleted).count();
        return done >= videoIds.size();
    }

    public boolean canAccessNextLesson(String candidateKeycloakId, Long themeId, Integer nextSequenceOrder) {
        List<Lesson> lessons = lessonRepository.findAll().stream()
                .filter(l -> l.getTheme() != null && l.getTheme().getId().equals(themeId))
                .collect(Collectors.toList());

        List<Long> resourceIds = lessons.stream()
                .flatMap(l -> l.getResources().stream())
                .filter(r -> r.getType() == ResourceType.QUIZ)
                .map(Resource::getId)
                .collect(Collectors.toList());

        if (resourceIds.isEmpty()) return false;

        long passedCount = quizAttemptRepository
                .countByCandidateKeycloakIdAndPassedTrueAndResource_IdIn(candidateKeycloakId, resourceIds);

        return passedCount >= 3;
    }

    public List<QuizQuestion> getQuizIfLessonConsumed(String candidateKeycloakId, Long lessonId) {
        List<Resource> resources = resourceRepository.findByLessonId(lessonId);
        List<Long> resourceIds = resources.stream().map(Resource::getId).toList();

        List<LessonProgress> progresses =
                lessonProgressRepository.findByCandidateKeycloakIdAndResource_IdIn(candidateKeycloakId, resourceIds);

        boolean consumed = !progresses.isEmpty()
                && progresses.stream().allMatch(LessonProgress::isCompleted);

        if (!consumed) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Leçon non consommée : accès au quiz refusé"
            );
        }

        // récupérer toutes les questions des ressources de la leçon
        return questionRepo.findByResource_Lesson_Id(lessonId);
    }

    public List<LessonProgressDTO> getCandidateLessonsWithProgress(String candidateKeycloakId, Long themeId) {
        // Récupérer les leçons du thème
        List<Lesson> lessons = lessonRepository.findByThemeIdOrderBySequenceOrderAsc(themeId);

        // Récupérer toute la progression du candidat
        List<LessonProgress> allProgress = lessonProgressRepository.findByCandidateKeycloakId(candidateKeycloakId);

        // Créer une map pour accès rapide: resourceId -> LessonProgress
        Map<Long, LessonProgress> progressMap = allProgress.stream()
                .collect(Collectors.toMap(
                        p -> p.getResource().getId(),
                        p -> p,
                        (p1, p2) -> p1
                ));

        return lessons.stream().map(lesson -> {
            List<Resource> resources = lesson.getResources();

            List<ResourceProgressDTO> resourceProgressList = resources.stream()
                    .map(resource -> {
                        LessonProgress progress = progressMap.get(resource.getId());
                        return ResourceProgressDTO.builder()
                                .resourceId(resource.getId())
                                .title(resource.getTitle())
                                .type(resource.getType())
                                .completed(progress != null && progress.isCompleted())
                                .completedAt(progress != null ? progress.getCompletedAt() : null)
                                .build();
                    })
                    .collect(Collectors.toList());

            int totalResources = resources.size();
            long completedCount = resourceProgressList.stream()
                    .filter(ResourceProgressDTO::isCompleted)
                    .count();

            Instant lastActivity = resourceProgressList.stream()
                    .map(ResourceProgressDTO::getCompletedAt)
                    .filter(java.util.Objects::nonNull)
                    .max(Instant::compareTo)
                    .orElse(null);

            double progressPercentage = totalResources > 0
                    ? (completedCount * 100.0) / totalResources
                    : 0;

            return LessonProgressDTO.builder()
                    .lessonId(lesson.getId())
                    .lessonTitle(lesson.getTitle())
                    .sequenceOrder(lesson.getSequenceOrder())
                    .themeId(themeId)
                    .totalResources(totalResources)
                    .completedResources((int) completedCount)
                    .progressPercentage(progressPercentage)
                    .fullyCompleted(completedCount == totalResources && totalResources > 0)
                    .lastActivityAt(lastActivity)
                    .resources(resourceProgressList)
                    .build();
        }).collect(Collectors.toList());
    }


    public LessonWithResourcesDTO getLessonWithResources(String candidateKeycloakId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Lesson not found with id: " + lessonId
                ));

        // Récupérer la progression du candidat pour cette leçon
        List<LessonProgress> progressList = lessonProgressRepository
                .findByCandidateKeycloakIdAndLesson_Id(candidateKeycloakId, lessonId);

        Map<Long, LessonProgress> progressMap = progressList.stream()
                .collect(Collectors.toMap(
                        p -> p.getResource().getId(),
                        p -> p,
                        (p1, p2) -> p1
                ));

        List<ResourceDTO> resourceDTOs = lesson.getResources().stream()
                .map(resource -> {
                    LessonProgress progress = progressMap.get(resource.getId());
                    return ResourceDTO.builder()
                            .id(resource.getId())
                            .title(resource.getTitle())
                            .url(resource.getUrl())
                            .type(resource.getType())
                            .passingScore(resource.getPassingScore())
                            .completed(progress != null && progress.isCompleted())
                            .completedAt(progress != null ? progress.getCompletedAt() : null)
                            .build();
                })
                .collect(Collectors.toList());

        return LessonWithResourcesDTO.builder()
                .lessonId(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .sequenceOrder(lesson.getSequenceOrder())
                .themeId(lesson.getTheme().getId())
                .resources(resourceDTOs)
                .build();
    }

    public ResourceDTO getResourceById(String candidateKeycloakId, Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource not found with id: " + resourceId
                ));

        // Vérifier que le candidat a accès à cette ressource
        LessonProgress progress = lessonProgressRepository
                .findByCandidateKeycloakIdAndResource_Id(candidateKeycloakId, resourceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Accès refusé : vous n'avez pas accès à cette ressource"
                ));

        return ResourceDTO.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .url(resource.getUrl())
                .type(resource.getType())
                .passingScore(resource.getPassingScore())
                .completed(progress.isCompleted())
                .completedAt(progress.getCompletedAt())
                .build();
    }




}
