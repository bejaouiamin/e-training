// File: services/cours/src/main/java/com/training/entities/Resource.java
package com.training.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lesson_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorKeycloakId;
    private String title;
    private String url; // lien vers le mp4 / pdf ou identifiant de stockage

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    // pour les quizzes : score minimum pour valider
    private Integer passingScore;

    @ManyToOne
    @JsonIgnore
    private Lesson lesson;
    // Relation ajoutée vers QuizQuestion
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<QuizQuestion> questions;
}
