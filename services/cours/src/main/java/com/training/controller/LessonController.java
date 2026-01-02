package com.training.controller;

import com.training.Dtos.LessonProgressDTO;
import com.training.Dtos.LessonWithResourcesDTO;
import com.training.Dtos.QuizSubmissionDTO;
import com.training.Dtos.ResourceDTO;
import com.training.entities.*;
import com.training.repository.LessonRepository;
import com.training.repository.ResourceRepository;
import com.training.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;
    private final LessonRepository lessonRepository;
    private final ResourceRepository resourceRepository;

    @PostMapping("/add")
    public ResponseEntity<Lesson> create(@RequestBody Lesson lesson) {
        Lesson saved = lessonService.createLesson(lesson);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/theme/{themeId}")
    public ResponseEntity<List<Lesson>> getLessonsByThemeId(@PathVariable Long themeId) {
        List<Lesson> lessons = lessonService.getLessonsByThemeId(themeId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(lesson);
    }


    @GetMapping("/{lessonId}/quiz")
    public List<QuizQuestion> getQuizForLesson(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId,
            @PathVariable Long lessonId) {
        return lessonService.getQuizIfLessonConsumed(candidateKeycloakId, lessonId);
    }

    @PostMapping("/resource/{resourceId}/complete")
    public ResponseEntity<Map<String, Object>> completeResource(@RequestParam String candidateKeycloakId, @PathVariable Long resourceId) {
        lessonService.markResourceCompleted(candidateKeycloakId, resourceId);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("/quiz/{resourceId}/attempt")
    public ResponseEntity<QuizAttempt> submitQuiz(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId,
            @PathVariable Long resourceId,
            @RequestBody List<Long> answerIds) {
        QuizAttempt attempt = lessonService.submitQuizAttempt(
                candidateKeycloakId,
                resourceId,
                answerIds
        );
        return ResponseEntity.ok(attempt);
    }



    @GetMapping("/quiz/can-open")
    public ResponseEntity<Map<String, Boolean>> canOpenQuiz(
            @RequestParam String candidateKeycloakId,
            @RequestParam Long lessonId) {
        boolean canOpen = lessonService.canOpenQuiz(candidateKeycloakId, lessonId);
        return ResponseEntity.ok(Map.of("canOpen", canOpen));
    }

    @GetMapping("/formateur/{keycloakId}")
    public ResponseEntity<List<Lesson>> getLessonsByFormateur(@PathVariable String keycloakId) {
        List<Lesson> lessons = lessonRepository.findByAuthorKeycloakId(keycloakId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/resources/formateur/{keycloakId}")
    public ResponseEntity<List<Resource>> getResourcesByFormateur(
            @PathVariable String keycloakId,
            @RequestParam(required = false) ResourceType type) {
        List<Resource> resources = (type != null)
                ? resourceRepository.findByAuthorKeycloakIdAndType(keycloakId, type)
                : resourceRepository.findByAuthorKeycloakId(keycloakId);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/next-lesson/can-access")
    public ResponseEntity<Map<String, Boolean>> canAccessNextLesson(
            @RequestParam String candidateKeycloakId,
            @RequestParam Long themeId,
            @RequestParam Integer nextSequenceOrder) {
        boolean canAccess = lessonService.canAccessNextLesson(candidateKeycloakId, themeId, nextSequenceOrder);
        return ResponseEntity.ok(Map.of("canAccess", canAccess));
    }

    @GetMapping("/progress/theme/{themeId}")
    public ResponseEntity<List<LessonProgressDTO>> getCandidateLessonsWithProgress(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId,
            @PathVariable Long themeId) {
        List<LessonProgressDTO> progress = lessonService.getCandidateLessonsWithProgress(candidateKeycloakId, themeId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/{lessonId}/resources")
    public ResponseEntity<LessonWithResourcesDTO> getLessonWithResources(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId,
            @PathVariable Long lessonId) {
        LessonWithResourcesDTO lesson = lessonService.getLessonWithResources(candidateKeycloakId, lessonId);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<ResourceDTO> getResourceById(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId,
            @PathVariable Long resourceId) {
        ResourceDTO resource = lessonService.getResourceById(candidateKeycloakId, resourceId);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/quiz/history")
    public ResponseEntity<List<QuizAttempt>> getCandidateQuizHistory(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId) {
        List<QuizAttempt> history = lessonService.getCandidateQuizHistory(candidateKeycloakId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/theme/{themeId}/enroll")
    public ResponseEntity<CandidateThemeEnrollment> enrollToTheme(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId,
            @PathVariable Long themeId) {
        return ResponseEntity.ok(lessonService.enrollCandidateToTheme(candidateKeycloakId, themeId));
    }

    @GetMapping("/themes/enrolled")
    public ResponseEntity<List<Theme>> getEnrolledThemes(
            @RequestHeader("X-Keycloak-Id") String candidateKeycloakId) {
        return ResponseEntity.ok(lessonService.getCandidateEnrolledThemes(candidateKeycloakId));
    }



}
