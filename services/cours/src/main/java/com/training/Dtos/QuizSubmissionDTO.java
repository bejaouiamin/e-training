package com.training.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionDTO {
    private String candidateKeycloakId;
    private Long quizResourceId;
    private List<Long> answers; // IDs des réponses sélectionnées
}