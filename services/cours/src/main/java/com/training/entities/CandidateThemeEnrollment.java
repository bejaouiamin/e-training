package com.training.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "candidate_theme_enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_keycloak_id", "theme_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateThemeEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_keycloak_id", nullable = false)
    private String candidateKeycloakId;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @Column(name = "enrolled_at")
    private Instant enrolledAt;
}
