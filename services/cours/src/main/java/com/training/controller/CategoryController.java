package com.training.controller;
import com.training.Dtos.CategoryDto;
import com.training.Dtos.ThemeDto;
import com.training.exeception.BadRequestException;
import com.training.service.CategoryService;
import com.training.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Object> create(@RequestBody CategoryDto dto) {
        try {
            CategoryDto created = categoryService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public List<CategoryDto> allcategory() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

}
