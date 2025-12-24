package com.training.service;
import com.training.Dtos.CategoryDto;
import com.training.entities.Category;
import com.training.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto create(CategoryDto dto) {
        Category category = CategoryDto.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return CategoryDto.fromEntity(saved);
    }

    public CategoryDto update(Long id, CategoryDto dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        Category saved = categoryRepository.save(existing);
        return CategoryDto.fromEntity(saved);
    }

    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(CategoryDto::fromEntity).collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
