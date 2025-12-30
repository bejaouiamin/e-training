package com.training.service;

import com.training.entities.Resource;
import com.training.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizQueryService {
    private final ResourceRepository resourceRepository;

    public List<com.training.Dtos.QuizResponse> getQuizzesByAuthor(String keycloakId) {
        List<Resource> quizzes = resourceRepository.findByAuthorKeycloakIdAndType(
                keycloakId, com.training.entities.ResourceType.QUIZ);
        return quizzes.stream().map(this::toDto).collect(Collectors.toList());
    }

    private com.training.Dtos.QuizResponse toDto(Resource r) {
        List<com.training.Dtos.QuizQuestionResponse> questions = r.getQuestions() == null ? List.of() :
                r.getQuestions().stream().map(q -> {
                    List<com.training.Dtos.QuizAnswerResponse> answers = q.getAnswers() == null ? List.of() :
                            q.getAnswers().stream()
                                    .map(a -> new com.training.Dtos.QuizAnswerResponse(
                                            a.getId(), a.getAnswerText(), a.isCorrect()))
                                    .collect(Collectors.toList());
                    return new com.training.Dtos.QuizQuestionResponse(q.getId(), q.getQuestionText(), answers);
                }).collect(Collectors.toList());

        return new com.training.Dtos.QuizResponse(
                r.getId(), r.getTitle(), r.getPassingScore(), r.getLesson().getId(),
                r.getAuthorKeycloakId(), questions);
    }
}
