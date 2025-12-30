package com.training.event;

import com.etraining.ThemeCreatedEvent;
import com.training.entities.Category;
import com.training.entities.Theme;
import com.training.repository.CategoryRepository;
import com.training.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeEventListener {
    private final ThemeRepository themeRepository;
    private final CategoryRepository categoryRepository;

    @KafkaListener(topics = "theme-created", groupId = "cours-group")
    public void onThemeCreated(ThemeCreatedEvent event) {
        log.info("Received ThemeCreatedEvent: {}", event.getTitle());

        Category category = categoryRepository.findById(event.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Theme theme = Theme.builder()
                .authorKeycloakId(event.getAuthorKeycloakId())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(category)
                .dureeHeures(event.getDureeHeures())
                .build();

        themeRepository.save(theme);
        log.info("Theme created with ID: {}", theme.getId());
    }
}
