package com.training.controller;

import com.training.Dtos.QuizResponse;
import com.training.service.QuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizQueryController {
    private final QuizQueryService quizQueryService;

    @GetMapping("/author/{keycloakId}")
    public List<QuizResponse> getQuizzesByAuthor(@PathVariable String keycloakId) {
        return quizQueryService.getQuizzesByAuthor(keycloakId);
    }
}
