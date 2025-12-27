// File: services/cours/src/main/java/com/training/entities/LessonProgress.java
package com.training.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "lesson_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;


    private boolean completed;

    private Instant completedAt;
}
