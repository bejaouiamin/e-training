package com.training.service;

import com.training.entities.*;
import com.training.event.EventPublisher;
import com.training.event.QuizSubmittedEvent;
import com.training.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ResourceRepository resourceRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Lesson createLesson(Lesson lesson) {
        // persist lesson + resources (cascade)
        return lessonRepository.save(lesson);
    }

    @Transactional
    public void markResourceCompleted(Long userId, Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        LessonProgress progress = lessonProgressRepository.findByUserIdAndResourceId(userId, resourceId)
                .orElse(LessonProgress.builder()
                        .userId(userId)
                        .resource(resource) // utiliser .resource() au lieu de .resourceId()
                        .completed(false)
                        .build());
        progress.setCompleted(true);
        progress.setCompletedAt(Instant.now());
        lessonProgressRepository.save(progress);
    }


    @Transactional
    public QuizAttempt submitQuizAttempt(Long userId, Long resourceId, Integer score) {
        Resource res = resourceRepository.findById(resourceId).orElseThrow();
        boolean passed = score != null && res.getPassingScore() != null && score >= res.getPassingScore();

        QuizAttempt attempt = QuizAttempt.builder()
                .userId(userId)
                .resource(res)
                .score(score)
                .passed(passed)
                .attemptedAt(Instant.now())
                .build();
        quizAttemptRepository.save(attempt);

        if (passed) {
            markResourceCompleted(userId, resourceId);
        }

        // publier l'événement après persistance
        QuizSubmittedEvent event = new QuizSubmittedEvent(
                userId,
                resourceId,
                score,
                passed,
                Instant.now(),
                "cours-service"
        );
        eventPublisher.publishQuizSubmitted(event);

        return attempt;
    }

    public boolean canOpenQuiz(Long userId, Long lessonId) {
        // rule: all VIDEO resources in lesson must be completed to open quizzes
        List<Resource> videos = resourceRepository.findByLessonIdAndType(lessonId, ResourceType.VIDEO);
        List<Long> videoIds = videos.stream().map(Resource::getId).collect(Collectors.toList());
        if (videoIds.isEmpty()) return true;
        List<LessonProgress> progresses = lessonProgressRepository.findByUserIdAndResourceIdIn(userId, videoIds);
        long done = progresses.stream().filter(LessonProgress::isCompleted).count();
        return done >= videoIds.size();
    }

    public boolean canAccessNextLesson(Long userId, Long themeId, Integer nextSequenceOrder) {
        // rule: user must have validated >=3 quizzes across the theme to access next lesson
        // collect all quiz resource ids in the theme (simple approach)
        // NOTE: org logic may be adapted: here we count passed quiz attempts for user in resources that belong to lessons of the theme
        List<Lesson> lessons = lessonRepository.findAll().stream()
                .filter(l -> l.getTheme() != null && l.getTheme().getId().equals(themeId))
                .collect(Collectors.toList());
        List<Long> resourceIds = lessons.stream()
                .flatMap(l -> l.getResources().stream())
                .filter(r -> r.getType() == ResourceType.QUIZ)
                .map(Resource::getId)
                .collect(Collectors.toList());
        if (resourceIds.isEmpty()) return false;
        long passedCount = quizAttemptRepository.countByUserIdAndPassedTrueAndResourceIdIn(userId, resourceIds);
        return passedCount >= 3;
    }
}
