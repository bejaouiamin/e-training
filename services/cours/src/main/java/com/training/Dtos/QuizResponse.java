package com.training.Dtos;

import java.util.List;

public record QuizResponse(Long id, String title, Integer passingScore, Long lessonId, String authorKeycloakId, List<QuizQuestionResponse> questions) { }