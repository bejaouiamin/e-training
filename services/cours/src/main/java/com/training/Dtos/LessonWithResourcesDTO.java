package com.training.Dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LessonWithResourcesDTO {
    private Long lessonId;
    private String title;
    private String description;
    private Integer sequenceOrder;
    private Long themeId;
    private List<ResourceDTO> resources;
}