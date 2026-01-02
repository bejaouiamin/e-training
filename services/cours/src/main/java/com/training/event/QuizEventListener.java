package com.training.event;

import com.etraining.QuizCreatedEvent;
import com.training.entities.*;
import com.training.repository.LessonRepository;
import com.training.repository.QuizAnswerRepository;
import com.training.repository.QuizQuestionRepository;
import com.training.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizEventListener {
    private final ResourceRepository resourceRepository;
    private final LessonRepository lessonRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    @KafkaListener(topics = "quiz-created", groupId = "cours-group")
    public void onQuizCreated(QuizCreatedEvent event) {
        log.info("Received QuizCreatedEvent: {}", event.getTitle());

        Lesson lesson = lessonRepository.findById(event.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Resource quiz = Resource.builder()
                .authorKeycloakId(event.getAuthorKeycloakId())
                .type(ResourceType.QUIZ)
                .title(event.getTitle())
                .passingScore(event.getPassingScore())
                .lesson(lesson)
                .build();

        Resource savedQuiz = resourceRepository.save(quiz);

        if (event.getQuestions() != null) {
            List<QuizQuestion> questions = event.getQuestions().stream()
                    .map(qDto -> {
                        QuizQuestion q = QuizQuestion.builder()
                                .questionText(qDto.getQuestionText())
                                .resource(savedQuiz)
                                .build();

                        List<QuizAnswer> answers = qDto.getAnswers().stream()
                                .map(aDto -> QuizAnswer.builder()
                                        .answerText(aDto.getAnswerText())
                                        .correct(aDto.isCorrect())
                                        .question(q)
                                        .build())
                                .toList();

                        q.setAnswers(answers);
                        return q;
                    })
                    .toList();

            quizQuestionRepository.saveAll(questions);
        }

        log.info("Quiz created with ID: {}", savedQuiz.getId());
    }

}
