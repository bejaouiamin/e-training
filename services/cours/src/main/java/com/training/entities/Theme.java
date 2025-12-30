package com.training.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "themes")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorKeycloakId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    // duration in hours
    private Integer dureeHeures;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Dans Theme.java (optionnel)
    @OneToMany(mappedBy = "theme")
    private List<Lesson> lessons;

}
