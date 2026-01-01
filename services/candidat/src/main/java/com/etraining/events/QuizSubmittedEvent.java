package com.etraining.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmittedEvent {
    private String KeycloakId;
    private Long quizResourceId;
    private Integer score;
    private boolean passed;
    private Instant submittedAt;
    private String source = "cours-service";
}

