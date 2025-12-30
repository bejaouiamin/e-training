package com.training.Dtos;

import java.util.List;

public record QuizQuestionResponse(Long id, String questionText, List<QuizAnswerResponse> answers) { }