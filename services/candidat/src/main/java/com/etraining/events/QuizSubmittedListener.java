package com.etraining.events;

import com.etraining.service.CandidatService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizSubmittedListener {
    private final CandidatService candidatService;

    @KafkaListener(topics = "quiz-submitted", groupId = "candidat-service", containerFactory = "kafkaListenerContainerFactory")
    public void handle(QuizSubmittedEvent event) {
        // traiter l'événement de façon idempotente
        candidatService.handleQuizSubmitted(event.getCandidateId(), event.getQuizResourceId(), event.getScore(), event.isPassed(), event.getSubmittedAt());
    }
}
