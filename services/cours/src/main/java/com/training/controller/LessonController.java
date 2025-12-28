package com.training.controller;

import com.training.entities.*;
import com.training.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

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
}
