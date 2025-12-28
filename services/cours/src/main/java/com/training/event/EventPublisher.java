// java
package com.training.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "quiz-submitted";

    public void publishQuizSubmitted(QuizSubmittedEvent event) {
        // envoyer l'objet: JsonSerializer sur le producer transformera en JSON correct (START_OBJECT)
        kafkaTemplate.send(TOPIC, event);
    }
}
