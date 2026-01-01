// File: services/cours/src/main/java/com/training/entities/QuizAttempt.java
package com.training.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "quiz_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateKeycloakId;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    @JsonIgnore
    private Resource resource;

    private Integer score;

    private boolean passed;

    private Instant attemptedAt;
}
