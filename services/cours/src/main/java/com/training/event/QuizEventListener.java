package com.training.event;

import com.etraining.QuizCreatedEvent;
import com.training.entities.Lesson;
import com.training.entities.Resource;
import com.training.entities.ResourceType;
import com.training.repository.LessonRepository;
import com.training.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizEventListener {
    private final ResourceRepository resourceRepository;
    private final LessonRepository lessonRepository;

    @KafkaListener(topics = "quiz-created", groupId = "cours-group")
    public void onQuizCreated(QuizCreatedEvent event) {
        log.info("Received QuizCreatedEvent: {}", event.getTitle());

        Lesson lesson = lessonRepository.findById(event.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Resource quiz = Resource.builder()
                .authorKeycloakId(event.getAuthorKeycloakId())
                .type(ResourceType.QUIZ)
                .title(event.getTitle())
                .url(event.getUrl())
                .passingScore(event.getPassingScore())
                .type(ResourceType.QUIZ)
                .lesson(lesson)
                .build();

        resourceRepository.save(quiz);
        log.info("Quiz created with ID: {}", quiz.getId());
    }
}
