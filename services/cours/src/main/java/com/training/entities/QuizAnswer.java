package com.training.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "quiz_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerText;

    @Column(name = "is_correct")
    private Boolean correct;  // Utilisez Boolean au lieu de boolean isCorrect

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private QuizQuestion question;

    // Getter explicite pour éviter les problèmes de nommage
    public boolean isCorrect() {
        return correct != null && correct;
    }
}

