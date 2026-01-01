package com.training.Dtos;

import com.training.entities.ResourceType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ResourceProgressDTO {
    private Long resourceId;
    private String title;
    private ResourceType type;
    private boolean completed;
    private Instant completedAt;
}
