package com.training.service;
import com.training.Dtos.ThemeDto;
import com.training.entities.Category;
import com.training.entities.Theme;
import com.training.exeception.BadRequestException;
import com.training.exeception.ResourceNotFoundException;
import com.training.repository.CategoryRepository;
import com.training.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final CategoryRepository categoryRepository;

    public ThemeDto create(ThemeDto dto) {
        if (dto.getCategoryId() == null) {
            throw new BadRequestException("categoryId is required");
        }
        Category cat = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException( "Category not found"));
        Theme theme = ThemeDto.toEntity(dto);
        theme.setCategory(cat);
        Theme saved = themeRepository.save(theme);
        return ThemeDto.fromEntity(saved);
    }

    public ThemeDto update(Long id, ThemeDto dto) {
        Theme existing = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setDureeHeures(dto.getDureeHeures());
        if (dto.getCategoryId() != null) {
            Category cat = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existing.setCategory(cat);
        }
        Theme saved = themeRepository.save(existing);
        return ThemeDto.fromEntity(saved);
    }

    public List<ThemeDto> findAll() {
        return themeRepository.findAll().stream().map(ThemeDto::fromEntity).collect(Collectors.toList());
    }

    public ThemeDto findById(Long id) {
        return themeRepository.findById(id).map(ThemeDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));
    }

    public List<ThemeDto> findByCategoryId(Long categoryId) {
        return themeRepository.findByCategoryId(categoryId).stream().map(ThemeDto::fromEntity).collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theme not found");
        }
        themeRepository.deleteById(id);
    }
}
