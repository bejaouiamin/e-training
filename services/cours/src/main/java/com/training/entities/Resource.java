// File: services/cours/src/main/java/com/training/entities/Resource.java
package com.training.entities;

import jakarta.persistence.*;
import lombok.*;

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

    private String title;
    private String url; // lien vers le mp4 / pdf ou identifiant de stockage

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    // pour les quizzes : score minimum pour valider
    private Integer passingScore;

    @ManyToOne
    private Lesson lesson;
}
