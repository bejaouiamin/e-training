package com.etraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreatedEvent {
    private String eventId;
    private String authorKeycloakId;
    private String title;
    private String url;
    private String resourceType;
    private Integer passingScore;
    private Long lessonId;
}

