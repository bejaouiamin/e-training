package com.training.controller;

import com.training.entities.*;
import com.training.repository.LessonRepository;
import com.training.repository.ResourceRepository;
import com.training.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
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

    @PostMapping("/resource/{resourceId}/complete")
    public ResponseEntity<Map<String, Object>> completeResource(@RequestParam Long userId, @PathVariable Long resourceId) {
        lessonService.markResourceCompleted(userId, resourceId);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("/quiz/{resourceId}/attempt")
    public ResponseEntity<QuizAttempt> submitQuiz(@RequestParam Long userId, @PathVariable Long resourceId, @RequestParam Integer score) {
        QuizAttempt attempt = lessonService.submitQuizAttempt(userId, resourceId, score);
        return ResponseEntity.ok(attempt);
    }

    @GetMapping("/quiz/can-open")
    public ResponseEntity<Map<String, Boolean>> canOpenQuiz(
            @RequestParam Long userId,
            @RequestParam Long lessonId) {
        boolean canOpen = lessonService.canOpenQuiz(userId, lessonId);
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
            @RequestParam Long userId,
            @RequestParam Long themeId,
            @RequestParam Integer nextSequenceOrder) {
        boolean canAccess = lessonService.canAccessNextLesson(userId, themeId, nextSequenceOrder);
        return ResponseEntity.ok(Map.of("canAccess", canAccess));
    }

}
