package com.training.formateur.Request;

import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {
    private String keycloakId;
    private String title;
    private Long lessonId;
    private Integer passingScore;
    private List<QuizQuestionRequest> questions;
}
