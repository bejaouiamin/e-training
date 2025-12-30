package com.training.formateur.service;

import com.etraining.LessonCreatedEvent;
import com.etraining.QuizCreatedEvent;
import com.etraining.ThemeCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishThemeCreated(ThemeCreatedEvent event) {
        Message<ThemeCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "theme-created")
                .setHeader(KafkaHeaders.KEY, event.getAuthorKeycloakId())
                .setHeader("__TypeId__", "theme")
                .build();

        log.info("Publishing ThemeCreatedEvent: {}", event);
        kafkaTemplate.send(message);
    }

    public void publishLessonCreated(LessonCreatedEvent event) {
        Message<LessonCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "lesson-created")
                .setHeader(KafkaHeaders.KEY, event.getAuthorKeycloakId())
                .setHeader("__TypeId__", "lesson")
                .build();

        log.info("Publishing LessonCreatedEvent: {}", event);
        kafkaTemplate.send(message);
    }

    public void publishQuizCreated(QuizCreatedEvent event) {
        Message<QuizCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "quiz-created")
                .setHeader(KafkaHeaders.KEY, event.getAuthorKeycloakId())
                .setHeader("__TypeId__", "quiz")
                .build();

        log.info("Publishing QuizCreatedEvent: {}", event);
        kafkaTemplate.send(message);
    }


}
