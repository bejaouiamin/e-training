package com.training.Dtos;

import com.training.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private List<ThemeDto> themes;

    public static CategoryDto fromEntity(Category category) {
        if (category == null) return null;
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .themes(category.getThemes() != null ? category.getThemes().stream()
                        .map(ThemeDto::fromEntity)
                        .toList() : null)

                .build();
    }
    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        if (dto.getThemes() != null) {
            category.setThemes(dto.getThemes().stream()
                    .map(ThemeDto::toEntity)
                    .collect(java.util.stream.Collectors.toSet()));
        }
        return category;
    }
//    public static Category EntityForCategory(CategoryDto dto) {
//        if (dto == null) return null;
//        Category category = new Category();
//        category.setId(dto.getId());
//        category.setName(dto.getName());
//        category.setDescription(dto.getDescription());
//        return category;
//    }
}
