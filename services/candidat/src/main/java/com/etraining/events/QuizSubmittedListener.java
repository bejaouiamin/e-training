package com.etraining.events;

import com.etraining.entity.Candidat;
import com.etraining.repository.CandidatRepository;
import com.etraining.service.CandidatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuizSubmittedListener {
    private final CandidatService candidatService;
    private final CandidatRepository candidatRepository;

    @KafkaListener(topics = "quiz-submitted", groupId = "candidat-service")
    public void handleQuizSubmitted(QuizSubmittedEvent event) {
        log.info("Received QuizSubmittedEvent: {}", event);

        if (event.isPassed()) {
            Optional<Candidat> candidatOpt = candidatRepository.findById(event.getCandidateId());
            if (candidatOpt.isPresent()) {
                Candidat candidat = candidatOpt.get();
                int currentCount = candidat.getPassedQuizCount() != null ? candidat.getPassedQuizCount() : 0;
                candidat.setPassedQuizCount(currentCount + 1);
                candidatRepository.save(candidat);
                log.info("Updated passedQuizCount for candidate {}: {}", event.getCandidateId(), candidat.getPassedQuizCount());
            } else {
                log.warn("Candidate not found: {}", event.getCandidateId());
            }
        } else {
            log.info("Quiz not passed, skipping update for candidate {}", event.getCandidateId());
        }
    }

}
