package com.etraining;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreatedEvent {
    private String id;
    private String authorKeycloakId;
    private String title;
    private Long lessonId;
    private Integer passingScore;
    private List<QuizQuestionDto> questions;
}
