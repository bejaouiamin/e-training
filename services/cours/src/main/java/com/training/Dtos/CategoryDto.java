// file: services/cours/src/main/java/com/training/Dtos/CategoryDto.java
package com.training.Dtos;

import com.training.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String icon; // nom du fichier d'icône
    private List<ThemeDto> themes;

    public static CategoryDto fromEntity(Category category) {
        if (category == null) return null;
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .themes(category.getThemes() != null ? category.getThemes().stream()
                        .map(ThemeDto::fromEntity)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIcon(dto.getIcon());
        if (dto.getThemes() != null) {
            category.setThemes(dto.getThemes().stream()
                    .map(ThemeDto::toEntity)
                    .collect(java.util.stream.Collectors.toSet()));
        }
        return category;
    }
}
