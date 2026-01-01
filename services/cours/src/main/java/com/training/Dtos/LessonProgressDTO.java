package com.training.Dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class LessonProgressDTO {
    private Long lessonId;
    private String lessonTitle;
    private Integer sequenceOrder;
    private Long themeId;
    private int totalResources;
    private int completedResources;
    private double progressPercentage;
    private boolean fullyCompleted;
    private Instant lastActivityAt;
    private List<ResourceProgressDTO> resources;
}
