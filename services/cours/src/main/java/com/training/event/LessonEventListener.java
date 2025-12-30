package com.training.event;

import com.etraining.LessonCreatedEvent;
import com.training.entities.Lesson;
import com.training.entities.Resource;
import com.training.entities.ResourceType;
import com.training.entities.Theme;
import com.training.repository.LessonRepository;
import com.training.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonEventListener {
    private final LessonRepository lessonRepository;
    private final ThemeRepository themeRepository;

    @KafkaListener(topics = "lesson-created", groupId = "cours-group")
    public void onLessonCreated(LessonCreatedEvent event) {
        log.info("Received LessonCreatedEvent: {}", event.getTitle());

        Theme theme = themeRepository.findById(event.getThemeId())
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        Lesson lesson = Lesson.builder()
                .authorKeycloakId(event.getAuthorKeycloakId())
                .title(event.getTitle())
                .description(event.getDescription())
                .sequenceOrder(event.getSequenceOrder())
                .theme(theme)
                .resources(new ArrayList<>())
                .build();

        // Convertir les ResourceDto en entités Resource
        if (event.getResources() != null) {
            List<Resource> resources = event.getResources().stream()
                    .map(resourceDto -> Resource.builder()
                            .authorKeycloakId(event.getAuthorKeycloakId())
                            .title(resourceDto.getTitle())
                            .url(resourceDto.getUrl())
                            .type(toEntityType(resourceDto.getType()))
                            .passingScore(resourceDto.getPassingScore())
                            .lesson(lesson)
                            .build())
                    .toList();

            lesson.setResources(resources);
        }

        lessonRepository.save(lesson);
        log.info("Lesson created with ID: {} and {} resources", lesson.getId(), lesson.getResources().size());
    }


    private ResourceType toEntityType(com.etraining.ResourceType type) {
        switch (type) {
            case VIDEO:
                return ResourceType.VIDEO;
            case PDF:
                return ResourceType.PDF;
            case QUIZ:
                return ResourceType.QUIZ;
            default:
                throw new IllegalArgumentException("Unknown ResourceType: " + type);
        }
    }
}

