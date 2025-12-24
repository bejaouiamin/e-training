package com.training.Dtos;

import com.training.entities.Theme;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {
    private Long id;
    private String titre;
    private String description;
    private Integer dureeHeures;
    @NotNull(message = "categoryId is required")
    private Long categoryId;

    public static ThemeDto fromEntity(Theme theme) {
        if (theme == null) return null;
        return ThemeDto.builder()
                .id(theme.getId())
                .titre(theme.getTitre())
                .description(theme.getDescription())
                .dureeHeures(theme.getDureeHeures())
                .categoryId(theme.getCategory() != null ? theme.getCategory().getId() : null)
                .build();
    }
    public static Theme toEntity(ThemeDto dto) {
            if (dto == null) return null;
            Theme theme = new Theme();
            theme.setId(dto.getId());
            theme.setTitre(dto.getTitre());
            theme.setDescription(dto.getDescription());
            theme.setDureeHeures(dto.getDureeHeures());
            theme.setCategory(dto.getCategoryId() != null ? new com.training.entities.Category() {{
                setId(dto.getCategoryId());
            }} : null);
            return theme;
        }
}