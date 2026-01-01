package com.training.Dtos;

import com.training.entities.ResourceType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ResourceDTO {
    private Long id;
    private String title;
    private String url;
    private ResourceType type;
    private Integer passingScore;
    private boolean completed;
    private Instant completedAt;
}
