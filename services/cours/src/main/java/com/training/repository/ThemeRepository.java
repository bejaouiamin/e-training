package com.training.repository;

import com.training.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findByCategoryId(Long categoryId);

    List<Theme> findByTitleContainingIgnoreCase(String keyword);
    List<Theme> findByAuthorKeycloakId(String authorKeycloakId);

}