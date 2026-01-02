package com.training.controller;

import com.training.Dtos.ThemeDto;
import com.training.entities.Theme;
import com.training.exeception.BadRequestException;
import com.training.exeception.ResourceNotFoundException;
import com.training.repository.ThemeRepository;
import com.training.service.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;
    private final ThemeRepository themeRepository;

    @PostMapping("/add")
    public ResponseEntity<Object> create(@RequestBody @Valid ThemeDto dto) {
        try {
            ThemeDto created = themeService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public List<ThemeDto> list() {
        return themeService.findAll();
    }

    @GetMapping("/{id}")
    public ThemeDto get(@PathVariable Long id) {
        return themeService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ThemeDto dto) {
        try {
            ThemeDto updated = themeService.update(id, dto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updated);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{categoryId}/bycategory")
    public List<ThemeDto> getByCategoryId(@PathVariable Long categoryId) {
        return themeService.findByCategoryId(categoryId);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ThemeDto>> getThemesByCategory(@PathVariable Long categoryId) {
        List<ThemeDto> themes = themeService.getThemesByCategory(categoryId);
        return ResponseEntity.ok(themes);
    }


    @GetMapping("/formateur/{keycloakId}")
    public ResponseEntity<List<Theme>> getThemesByFormateur(@PathVariable String keycloakId) {
        List<Theme> themes = themeRepository.findByAuthorKeycloakId(keycloakId);
        return ResponseEntity.ok(themes);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        themeService.delete(id);
    }
}
