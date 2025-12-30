package com.training.formateur.Request;

import lombok.Data;

@Data
public class QuizRequest {
    private String keycloakId;
    private String title;
    private String url;
    private String resourceType;
    private Integer passingScore;
    private Long lessonId;
}
