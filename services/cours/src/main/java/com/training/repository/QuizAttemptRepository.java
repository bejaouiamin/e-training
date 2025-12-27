package com.training.repository;

import com.training.entities.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    // Utiliser resource.id car QuizAttempt a maintenant @ManyToOne Resource
    List<QuizAttempt> findByUserIdAndResourceId(Long userId, Long resourceId);

    // AJOUTER le paramètre userId manquant
    long countByUserIdAndPassedTrue(Long userId);

    // Utiliser resource.id pour la recherche IN
    long countByUserIdAndPassedTrueAndResourceIdIn(Long userId, List<Long> resourceIds);
}

